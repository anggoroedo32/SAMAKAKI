package com.qatros.samakaki.response

import com.google.gson.annotations.SerializedName

data class FindUserResponse(

	@field:SerializedName("data")
	val data: DataBiodataUser? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class BiodataUser(

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
	val marriageStatus: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataBiodataUser(

	@field:SerializedName("biodata")
	val biodata: BiodataUser? = null
)
