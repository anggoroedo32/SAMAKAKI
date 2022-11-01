package com.awp.samakaki.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class BiodataResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(
	@field:SerializedName("biodata")
	val biodata: Biodata? = null
)

@Parcelize
data class Biodata(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("dob")
	val dob: String? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null,

	@field:SerializedName("marriage_status")
	val marriageStatus: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable
