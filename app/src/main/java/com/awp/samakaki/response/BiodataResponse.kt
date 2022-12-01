package com.awp.samakaki.response

import com.google.gson.annotations.SerializedName

data class BiodataResponse(

    @field:SerializedName("data")
    val data: DataUser? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class BiodataUserList(

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("dob")
    val dob: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("avatar")
    val avatar: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("marriage_status")
    val marriageStatus: Any? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class DataUser(

    @field:SerializedName("biodata")
    val biodata: BiodataUserList? = null
)
