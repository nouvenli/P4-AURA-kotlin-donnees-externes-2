package com.aura.ui.transfer

data class TransferData(
    val recipient: String = "",
    val amount: String = "",
    val isButtonEnabled: Boolean = false
)