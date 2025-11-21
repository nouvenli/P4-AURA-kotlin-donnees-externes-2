package com.aura.domain.usecase

import com.aura.data.mapper.toDto
import com.aura.domain.model.UserCredentials
import com.aura.domain.repository.AuraRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuraRepository
) {
    suspend operator fun invoke(credentials: UserCredentials): Boolean {
        val credentialsDto = credentials.toDto()
        return repository.login(credentialsDto.id, credentialsDto.password)
    }
}