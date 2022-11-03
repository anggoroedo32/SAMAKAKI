package com.awp.samakaki.request

import com.google.gson.annotations.SerializedName

data class editProfileRequest(
    @SerializedName("address")
    val address: String,

)