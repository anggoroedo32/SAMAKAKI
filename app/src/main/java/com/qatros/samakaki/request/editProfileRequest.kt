package com.qatros.samakaki.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class editProfileRequest(

@SerializedName("address")
val address: String,
@SerializedName("dob")
val dob: String,
@SerializedName("marriage_status")
val marriageStatus: String,
@SerializedName("status")
val status: String,
@SerializedName("avatar")
val avatar: String
) : Parcelable