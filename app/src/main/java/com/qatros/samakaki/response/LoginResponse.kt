package com.qatros.samakaki.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class LoginResponse(

    @field:SerializedName("data")
    val dataLogin: DataLogin,

    @field:SerializedName("status")
    val status: String? = null
)

data class DataLogin(
    @field:SerializedName("user")
    val users: UserLogin,

    @field:SerializedName("token")
    val token: String? = null
)

@Parcelize
data class UserLogin(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("email")
    val email: String? = null
) : Parcelable

data class MessageLoginResponse(

    @field:SerializedName("message")
    val message: String? = null
)