package com.qatros.samakaki.request

import com.google.gson.annotations.SerializedName

data class PostRequest(
    @SerializedName("descriptions")
    var descriptions: String,
    @SerializedName("status")
    var status: String,
)
