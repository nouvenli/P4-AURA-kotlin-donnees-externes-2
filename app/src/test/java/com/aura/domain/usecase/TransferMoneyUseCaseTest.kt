package com.aura.domain.usecase

import com.aura.domain.model.MoneyTransfer
import com.aura.domain.repository.AuraRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any

class TransferMoneyUseCaseTest {
    @Mock
    private lateinit var repository: AuraRepository

    private lateinit var transferMoneyUseCase: TransferMoneyUseCase

    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)
        transferMoneyUseCase = TransferMoneyUseCase(repository)
    }

    @Test
    fun `invoke should return true when transfer is successful`() = runTest {
        //GIVEN
        `when`(repository.transfer(any(), any(), any())).thenReturn(true)
        val moneyTransfer = MoneyTransfer("1234", "5678", 100.0)


        //WHEN
        val result = transferMoneyUseCase(moneyTransfer)

        //THEN
        assertEquals(true, result)
    }

    @Test
    fun `invoke should return false when transfer fails`()=runTest {
        //GIVEN
        `when`(repository.transfer(any(), any(), any())).thenReturn(false)
        val moneyTransfer = MoneyTransfer("1234", "invalid", 100.0)

        //WHEN
        val result = transferMoneyUseCase(moneyTransfer)

        //THEN
        assertEquals(false, result)

    }


}