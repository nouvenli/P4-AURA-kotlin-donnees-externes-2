package com.aura.domain.model

data class UserAccount(
    val accountId: String,
    val isMainAccount: Boolean,
    val balance: Double
)
