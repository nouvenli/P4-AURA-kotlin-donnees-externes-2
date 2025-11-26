package com.aura.data.model

import com.google.gson.annotations.SerializedName

data class CredentialsDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("password")
    val password: String
)
