package com.aura.ui.login

data class LoginUIState(
    val identifier: String = "",
    val password: String = "",
    val isButtonEnabled: Boolean = false,
    val isLoading: Boolean = false
)
