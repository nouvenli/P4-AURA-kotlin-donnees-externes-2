package com.aura.domain.usecase

import com.aura.data.mapper.toDto
import com.aura.domain.model.MoneyTransfer
import com.aura.domain.repository.AuraRepository
import javax.inject.Inject

class TransferMoneyUseCase @Inject constructor(
    private val repository: AuraRepository
) {
    suspend operator fun invoke(transfer: MoneyTransfer): Boolean {
        val transferDto = transfer.toDto()
        return repository.transfer(
            transferDto.sender,
            transferDto.recipient,
            transferDto.amount
        )
    }
}