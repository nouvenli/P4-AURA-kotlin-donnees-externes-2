package com.aura.data.repository

import com.aura.data.mapper.toDomain
import com.aura.data.model.CredentialsDto
import com.aura.data.model.TransferDto
import com.aura.data.remote.AuraApiService
import com.aura.domain.model.UserAccount
import com.aura.domain.repository.AuraRepository
import javax.inject.Inject

class AuraRepositoryImpl @Inject constructor(
    private val apiService: AuraApiService
) : AuraRepository {

    override suspend fun login(id: String, password: String): Boolean {
        val credentials = CredentialsDto(id, password)
        val result = apiService.login(credentials)
        return result.granted
    }

    override suspend fun getAccount(userId: String): List<UserAccount> {
        val accountsDto = apiService.getAccount(userId)
        return accountsDto.toDomain()
    }

    override suspend fun transfer(sender: String, recipient: String, amount: Double): Boolean {
        val transfer = TransferDto(sender, recipient, amount)
        val result = apiService.transfer(transfer)
        return result.result
    }
}