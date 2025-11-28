package com.aura.data.mapper

import com.aura.data.model.AccountDto
import com.aura.domain.model.UserAccount
import org.junit.Assert.assertEquals
import org.junit.Test


class AccountMapperTest {

    @Test
    fun `toDomain should convert AccountDto to UserAccount`() {
        //GIVEN - object AccountDto
        val accountDto = AccountDto(
            id = "123",
            main = true,
            balance = 100.50
        )
        //WHEN - Convert object AccountDto en UserAccount
        val userAccount = accountDto.toDomain()

        //THEN - Verify that the attributes are converted correctly.
        assertEquals("123", userAccount.accountId)
        assertEquals(true, userAccount.isMainAccount)
        assertEquals(100.50, userAccount.balance, 0.01)
    }

    @Test
    fun `toDomain Should convert empty list AccountDto to empty list`() {
        //GIVEN - Empty list of AccountDto
        val accountDtoList = emptyList<AccountDto>()

        //WHEN - Convert object AccountDto en UserAccount
        val userAccountList = accountDtoList.toDomain()

        //THEN - Verify that the attributes are converted correctly.
        assertEquals(0, userAccountList.size)
    }


    @Test
    fun `toDomain convert list AccountDto to list of UserAccount`() {
        //GIVEN - List of object AccountDto
        val accountDtoList = listOf(
            AccountDto(id = "1", main = true, balance = 1001.0),
            AccountDto(id = "2", main = false, balance = 2002.0),
            AccountDto(id = "3", main = false, balance = 3003.10)
        )
        //WHEN - Convert List of object AccountDto en List of UserAccount
        val userAccountList = accountDtoList.toDomain()

        //THEN - Verify that the attributes are converted correctly.
        assertEquals(3, userAccountList.size)
        for (i in accountDtoList.indices) {
            assertEqualsAccount(accountDtoList[i], userAccountList[i])
        }
    }

    // --- Helpers ---

    private fun assertEqualsAccount(expected: AccountDto, actual: UserAccount) {
        assertEquals("wrong id", expected.id, actual.accountId)
        assertEquals("wrong status main", expected.main, actual.isMainAccount)
        assertEquals("wrong balance", expected.balance, actual.balance, 0.01)
    }
}