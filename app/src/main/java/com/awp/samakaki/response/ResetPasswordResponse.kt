package com.awp.samakaki.response


import com.google.gson.annotations.SerializedName

data class ResetPasswordResponse(
    @SerializedName("data")
    val `data`: String,
    @SerializedName("status")
    val status: String
)

data class MessageResetPasswordResponse(

    @field:SerializedName("message")
    val message: String? = null
)
