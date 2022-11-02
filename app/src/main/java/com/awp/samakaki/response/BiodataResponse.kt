package com.awp.samakaki.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.File


data class BiodataResponse(

	@field:SerializedName("data")
	val dataBiodata: DataBiodata? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataBiodata(
	@field:SerializedName("biodata")
	val biodata: Biodata? = null,

	@field:SerializedName("token")
	val token: String? = null
)

@Parcelize
data class Biodata(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("dob")
	val dob: String? = null,

	@field:SerializedName("avatar")
	val avatar: File? = null,

	@field:SerializedName("marriage_status")
	val marriageStatus: String? = null,

	@field:SerializedName("status")
	val status: String? = null,
) : Parcelable
