package com.aura.domain.model

data class MoneyTransfer(
    val senderId : String,
    val recipientId : String,
    val amount : Double
)
