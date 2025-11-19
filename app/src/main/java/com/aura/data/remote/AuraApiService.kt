package com.aura.data.remote

import com.aura.data.model.Account
import com.aura.data.model.Credentials
import com.aura.data.model.CredentialsResult
import com.aura.data.model.Transfer
import com.aura.data.model.TransferResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuraApiService {

    @POST("login")
    suspend fun login(@Body credentials: Credentials): CredentialsResult

    @GET("accounts/{id}")
    suspend fun getAccount(@Path("id") userId: String): List<Account>

    @POST("transfer")
    suspend fun transfer(@Body transfer: Transfer): TransferResult
}
