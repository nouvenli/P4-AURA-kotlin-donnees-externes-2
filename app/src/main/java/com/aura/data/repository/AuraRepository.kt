package com.aura.data.repository

import com.aura.data.model.Account
import com.aura.data.model.Credentials
import com.aura.data.model.Transfer
import com.aura.data.remote.AuraApiService
import javax.inject.Inject


class AuraRepository @Inject constructor(private val apiService: AuraApiService) {

    suspend fun login(id: String, password: String): Boolean {

            val credentials = Credentials(id, password)
            val result = apiService.login(credentials)
            return result.granted
    }
    suspend fun getAccount(userId: String): List<Account> {
            return apiService.getAccount(userId)
    }

    suspend fun transfer(sender: String, recipient: String, amount: Double): Boolean {

            val transfer = Transfer(sender, recipient, amount)
            val result = apiService.transfer(transfer)
            return result.result
    }
}