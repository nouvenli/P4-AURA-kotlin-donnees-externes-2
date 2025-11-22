package com.aura.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.R
import com.aura.domain.usecase.GetAccountUseCase
import com.aura.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,  // ← Use Case au lieu du Repository
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<HomeData>>(UiState.Loading)
    val uiState: StateFlow<UiState<HomeData>> = _uiState.asStateFlow()

    fun loadBalance(userId: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                // TODO: RETIRER EN PRODUCTION - Délai de test pour le ProgressBar
                kotlinx.coroutines.delay(2000)

                // Appeler le Use Case (retourne directement List<UserAccount>)
                val accounts = getAccountUseCase(userId)

                if (accounts.isEmpty()) {
                    _uiState.value = UiState.Error(context.getString(R.string.error_network))
                    return@launch
                }

                // Trouver le compte principal
                val mainAccount = accounts.firstOrNull { it.isMainAccount }

                if (mainAccount == null) {
                    _uiState.value = UiState.Error(context.getString(R.string.error_generic))
                    return@launch
                }

                // Formater le solde
                val balanceText = String.format("%.2f€", mainAccount.balance)
                _uiState.value = UiState.Success(HomeData(balanceFormatted = balanceText))

            } catch (e: Exception) {
                _uiState.value = UiState.Error(context.getString(R.string.error_network))
            }
        }
    }
}