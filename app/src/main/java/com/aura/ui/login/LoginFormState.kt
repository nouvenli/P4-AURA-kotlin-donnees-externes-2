package com.aura.ui.login

data class LoginFormState(
    val identifier: String = "",
    val password: String = "",
    val isButtonEnabled: Boolean = false
)
