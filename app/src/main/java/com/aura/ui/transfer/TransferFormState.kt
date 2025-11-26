package com.aura.ui.transfer

data class TransferFormState(
    val recipient: String = "",
    val amount: String = "",
    val isButtonEnabled: Boolean = false
)