package com.awp.samakaki.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EditProfileResponse(

	@field:SerializedName("databiodata")
	val data: DataBiodata? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class Biodata(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("dob")
	val dob: String? = null,

	@field:SerializedName("marriage_status")
	val marriageStatus: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class DataBiodata(

	@field:SerializedName("biodata")
	val biodata: Biodata? = null
) : Parcelable
