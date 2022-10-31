package com.awp.samakaki.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BiodataRequest(
    @SerializedName("address")
    val address: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("marriage_status")
    val marriageStatus: String,
    @SerializedName("status")
    val status: String
) : Parcelable
