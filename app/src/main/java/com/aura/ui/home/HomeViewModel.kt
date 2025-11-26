package com.aura.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.R
import com.aura.domain.model.UserAccount
import com.aura.domain.usecase.GetAccountUseCase
import com.aura.ui.model.UiStateWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * --- Dependencies ---
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    /**
     * --- ui form state & state (loading, success, error) ---
     */

    private val _uiState = MutableStateFlow<UiStateWrapper<HomeFormState>>(UiStateWrapper.Idle)
    val uiState: StateFlow<UiStateWrapper<HomeFormState>> = _uiState.asStateFlow()

    private var userId: String = ""

    /**
     * --- Initialization ---
     */

    fun setUserId(id: String) {
        userId = id
        loadBalance()
    }

    val currentUserId: String
        get() = userId

    /**
     * --- Business Actions ---
     */
    fun loadBalance() {
        if (userId.isBlank()) {
            showError(R.string.error_generic)
            return
        }

        showLoading()

        viewModelScope.launch {
            try {
                val account = fetchMainAccount(userId)
                val formattedBalance = formatBalance(account.balance)
                showSuccess(HomeFormState(balanceFormatted = formattedBalance))
            } catch (e: Exception) {
                showError(R.string.error_network)

            }
        }
    }


    private suspend fun fetchMainAccount(userId: String): UserAccount {

        val accounts = getAccountUseCase(userId)

        if (accounts.isEmpty())
            throw Exception("EMPTY_ACCOUNTS")

        return accounts.firstOrNull { it.isMainAccount }
            ?: throw Exception("NO_MAIN_ACCOUNT")
    }

    /**
     * --- helpers ---
     */


    private fun showLoading() {
        _uiState.value = UiStateWrapper.Loading
    }

    private fun showSuccess(data: HomeFormState) {
        _uiState.value = UiStateWrapper.Success(data)
    }

    private fun showError(messageResId: Int) {
        _uiState.value = UiStateWrapper.Error(context.getString(messageResId))
    }

    private fun formatBalance(balance: Double): String {
        return String.format("%.2f €", balance)
    }
}
