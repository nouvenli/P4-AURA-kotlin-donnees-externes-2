package com.aura.domain.usecase

import com.aura.domain.model.UserAccount
import com.aura.domain.repository.AuraRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any


class GetAccountUseCaseTest {
    @Mock
    private lateinit var repository: AuraRepository

    private lateinit var getAccountUseCase: GetAccountUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getAccountUseCase = GetAccountUseCase(repository)
    }

    @Test
    fun `invoke should return a list of UserAccount when repository returns accounts`() = runTest {
        val userId = "1234"

        // GIVEN
        val expectedAccounts = listOf(
            UserAccount("1", true, 100.0),
            UserAccount("2", false, 200.0)
        )
        `when`(repository.getAccount(any())).thenReturn(expectedAccounts)

        //WHEN
        val result = getAccountUseCase(userId)

        //THEN
        assertEquals(2, result.size)
        assertEquals("1", result[0].accountId)
        assertEquals(true, result[0].isMainAccount)
        assertEquals(100.0, result[0].balance,0.01)
        assertEquals("2", result[1].accountId)
        assertEquals(false, result[1].isMainAccount)
        assertEquals(200.0, result[1].balance,0.01)
    }

    @Test
    fun `invoke should return an empty list when repository returns no accounts`() = runTest {
        // GIVEN
        val userId = "1234"
        `when`(repository.getAccount(any())).thenReturn(emptyList())

        //WHEN
        val result = getAccountUseCase(userId)

        //THEN
        assertEquals(0, result.size)
    }

}
