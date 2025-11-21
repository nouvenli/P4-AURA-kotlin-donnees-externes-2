package com.aura.data.mapper

import com.aura.data.model.AccountDto
import com.aura.domain.model.UserAccount

fun AccountDto.toDomain(): UserAccount {
    return UserAccount(
        accountId = this.id,
        isMainAccount = this.main,
        balance = this.balance
    )
}

fun List<AccountDto>.toDomain(): List<UserAccount> {
    return this.map { it.toDomain() }
}