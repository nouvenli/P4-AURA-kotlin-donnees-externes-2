package com.aura.data.mapper

import com.aura.data.model.AccountDto
import com.aura.domain.model.UserAccount
import org.junit.Assert.assertEquals
import org.junit.Test


class AccountMapperTest {

    @Test
    fun `toDomain should convert AccountD to UserAccount`() {
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
        assertEquals("1", userAccountList[0].accountId)
        assertEquals(true, userAccountList[0].isMainAccount)
        assertEquals(1001.0, userAccountList[0].balance, 0.01)
        assertEquals("2", userAccountList[1].accountId)
        assertEquals(false, userAccountList[1].isMainAccount)
        assertEquals(2002.0, userAccountList[1].balance, 0.01)
        assertEquals("3", userAccountList[2].accountId)
        assertEquals(false, userAccountList[2].isMainAccount)
        assertEquals(3003.10, userAccountList[2].balance, 0.01)
    }
}