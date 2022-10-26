package com.awp.samakaki.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class LoginResponse(

    @field:SerializedName("dataLogin")
    val dataLogin: DataLogin? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class DataLogin(
    @field:SerializedName("users")
    val users: Users? = null,

    @field:SerializedName("token")
    val token: String? = null
)

@Parcelize
data class Users(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("email")
    val email: String? = null
) : Parcelable