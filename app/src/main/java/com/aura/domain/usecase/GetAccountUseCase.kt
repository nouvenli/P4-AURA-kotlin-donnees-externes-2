package com.aura.domain.usecase

import com.aura.domain.model.UserAccount
import com.aura.domain.repository.AuraRepository
import javax.inject.Inject

class GetAccountUseCase @Inject constructor(
    private val repository: AuraRepository
) {
    suspend operator fun invoke(userId: String): List<UserAccount> {
        return repository.getAccount(userId)
    }
}