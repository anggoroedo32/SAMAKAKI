package com.awp.samakaki.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class BiodataResponse(
    @SerializedName("data")
    val data: Data? = null,

    @SerializedName("biodata")
    val biodata: List<Biodata>? = null
)

@Parcelize
data class Biodata(
    @SerializedName("address")
    val address: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("marriage_status")
    val marriageStatus: String,
    @SerializedName("status")
    val status: String
) : Parcelable