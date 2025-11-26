package com.aura.data.model

import com.google.gson.annotations.SerializedName

data class CredentialsResultDto(
    @SerializedName("granted")
    val granted: Boolean
)
