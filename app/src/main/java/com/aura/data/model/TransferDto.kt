package com.aura.data.model

import com.google.gson.annotations.SerializedName

data class TransferDto(
    @SerializedName("sender")
    val sender: String,

    @SerializedName("recipient")
    val recipient: String,

    @SerializedName("amount")
    val amount: Double
)
