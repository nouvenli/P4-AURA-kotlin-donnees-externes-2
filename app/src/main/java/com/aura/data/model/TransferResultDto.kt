package com.aura.data.model

import com.google.gson.annotations.SerializedName

data class TransferResultDto(
    @SerializedName("result")
    val result: Boolean
)
