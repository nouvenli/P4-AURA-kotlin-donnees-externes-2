package com.aura.domain.usecase

import com.aura.domain.model.UserCredentials
import com.aura.domain.repository.AuraRepository

import kotlinx.coroutines.test.runTest

import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any


class LoginUseCaseTest {

    @Mock
    private lateinit var repository: AuraRepository

    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        loginUseCase = LoginUseCase(repository)
    }

    @Test
    fun `invoke should return true when login is successful`() = runTest {
        // GIVEN
        `when`(repository.login(any(), any())).thenReturn(true)
        val credentials = UserCredentials("1234", "p@sswOrd")

        //WHEN
        val result = loginUseCase(credentials)

        //THEN
        assertEquals(true, result)
    }

    @Test
    fun `invoke should return false when credentials are invalid`() = runTest {
        // GIVEN
        `when`(repository.login(any(), any())).thenReturn(false)
        val credentials = UserCredentials("invalidUser", "invalidPassword")

        //WHEN
        val result = loginUseCase(credentials)

        //THEN
        assertEquals(false, result)
    }

}