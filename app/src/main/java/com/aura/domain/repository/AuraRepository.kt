package com.aura.domain.repository

import com.aura.domain.model.UserAccount

interface AuraRepository {
    suspend fun login(id: String, password: String): Boolean
    suspend fun getAccount(userId: String): List<UserAccount>
    suspend fun transfer(sender: String, recipient: String, amount: Double): Boolean
}