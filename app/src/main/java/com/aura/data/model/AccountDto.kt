package com.aura.data.model

import com.google.gson.annotations.SerializedName

data class AccountDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("main")
    val main: Boolean,

    @SerializedName("balance")
    val balance: Double
)
