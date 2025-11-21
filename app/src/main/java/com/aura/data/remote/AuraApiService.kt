package com.aura.data.remote

import com.aura.data.model.AccountDto
import com.aura.data.model.CredentialsDto
import com.aura.data.model.CredentialsResultDto
import com.aura.data.model.TransferDto
import com.aura.data.model.TransferResultDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuraApiService {

    @POST("login")
    suspend fun login(@Body credentials: CredentialsDto): CredentialsResultDto

    @GET("accounts/{id}")
    suspend fun getAccount(@Path("id") userId: String): List<AccountDto>

    @POST("transfer")
    suspend fun transfer(@Body transfer: TransferDto): TransferResultDto
}
