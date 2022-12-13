package com.qatros.samakaki.request

import com.google.gson.annotations.SerializedName

class RegisterWithTokenRequest (

    @SerializedName("name")
    var name: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("phone")
    var phone: String,

    @SerializedName("password")
    var password: String,

    @SerializedName("token")
    var token: String,

)