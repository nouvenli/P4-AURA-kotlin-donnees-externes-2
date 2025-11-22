package com.aura.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.R
import com.aura.domain.model.UserCredentials
import com.aura.domain.usecase.LoginUseCase
import com.aura.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,  // ← Use Case au lieu du Repository
    @ApplicationContext private val context: Context
) : ViewModel() {


    private val _loginData = MutableStateFlow(LoginData())
    val loginData: StateFlow<LoginData> = _loginData.asStateFlow()

    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val loginState: StateFlow<UiState<Unit>> = _loginState.asStateFlow()

    fun onIdentifierChange(newIdentifier: String) {
        _loginData.update { currentState ->
            currentState.copy(
                identifier = newIdentifier,
                isButtonEnabled = isFormValid(newIdentifier, currentState.password)
            )
        }
    }

    fun onPasswordChange(newPassword: String) {
        _loginData.update { currentState ->
            currentState.copy(
                password = newPassword,
                isButtonEnabled = isFormValid(currentState.identifier, newPassword)
            )
        }
    }

    private fun isFormValid(identifier: String, password: String): Boolean {
        return identifier.isNotBlank() && password.isNotBlank()
    }

    fun connect() {
        _loginState.value = UiState.Loading
        viewModelScope.launch {
            // TODO: RETIRER EN PRODUCTION - Délai de test pour le ProgressBar
            kotlinx.coroutines.delay(2000)
            try {
                // Créer l'objet Domain
                val credentials = UserCredentials(
                    userId = _loginData.value.identifier,
                    password = _loginData.value.password
                )

                // Appeler le Use Case
                val success = loginUseCase(credentials)

                if (success) {
                    _loginState.value = UiState.Success(Unit)
                } else {
                    _loginState.value = UiState.Error(context.getString(R.string.error_invalid_credentials))
                }
            } catch (e: Exception) {
                _loginState.value = UiState.Error(context.getString(R.string.error_network))
            }
        }
    }

    fun getUserId(): String = _loginData.value.identifier
}