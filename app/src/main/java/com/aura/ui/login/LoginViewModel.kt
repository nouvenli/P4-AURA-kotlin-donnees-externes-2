package com.aura.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.R
import com.aura.domain.model.UserCredentials
import com.aura.domain.usecase.LoginUseCase
import com.aura.ui.model.UiStateWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * --- Dependencies ---
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    /**
     * --- ui form state & state (loading, success, error) ---
     */

    private val _loginFormState = MutableStateFlow(LoginFormState())
    val loginFormState: StateFlow<LoginFormState> = _loginFormState.asStateFlow()

    private val _loginState = MutableStateFlow<UiStateWrapper<Unit>>(UiStateWrapper.Idle)
    val loginState: StateFlow<UiStateWrapper<Unit>> = _loginState.asStateFlow()

    /**
     * --- Form updates ---
     */

    fun onIdentifierChange(newIdentifier: String) {
        _loginFormState.update { currentState ->
            currentState.copy(
                identifier = newIdentifier,
                isButtonEnabled = isFormValid(newIdentifier, currentState.password)
            )
        }
    }

    fun onPasswordChange(newPassword: String) {
        _loginFormState.update { currentState ->
            currentState.copy(
                password = newPassword,
                isButtonEnabled = isFormValid(currentState.identifier, newPassword)
            )
        }
    }

    private fun isFormValid(id: String, pwd: String): Boolean {
        return id.isNotBlank() && pwd.isNotBlank()
    }

    /**
     * --- Business Actions ---
     */

    fun connect() {

        showLoading()

        viewModelScope.launch {
            try {
                val credentials = buildCredentials()
                val success = loginUseCase(credentials)

                if (success) showSuccess() else showError(R.string.error_invalid_credentials)
            } catch (e: Exception) {
                showError(R.string.error_network)
            }
        }
    }

    private fun buildCredentials(): UserCredentials {
        return UserCredentials(
            userId = loginFormState.value.identifier,
            password = loginFormState.value.password
        )
    }

    /**
     *     --- helpers ---
     */

    private fun showLoading() {
        _loginState.value = UiStateWrapper.Loading
    }

    private fun showSuccess() {
        _loginState.value = UiStateWrapper.Success(Unit)
    }

    private fun showError(msgRes: Int) {
        _loginState.value = UiStateWrapper.Error(context.getString(msgRes))
    }
}