package com.qatros.samakaki.request


import com.google.gson.annotations.SerializedName

data class ForgotTokenRequest(
    @SerializedName("email")
    val email: String
)