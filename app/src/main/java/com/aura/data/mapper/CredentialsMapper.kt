package com.aura.data.mapper

import com.aura.data.model.CredentialsDto
import com.aura.domain.model.UserCredentials

fun UserCredentials.toDto(): CredentialsDto {
    return CredentialsDto(
        id = this.userId,
        password = this.password
    )
}