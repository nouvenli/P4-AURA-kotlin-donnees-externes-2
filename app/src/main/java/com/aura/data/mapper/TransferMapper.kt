package com.aura.data.mapper

import com.aura.data.model.TransferDto
import com.aura.domain.model.MoneyTransfer

fun MoneyTransfer.toDto(): TransferDto {
    return TransferDto(
        sender = this.senderId,
        recipient = this.recipientId,
        amount = this.amount
    )
}