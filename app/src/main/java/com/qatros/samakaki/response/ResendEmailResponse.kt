package com.qatros.samakaki.response

import com.google.gson.annotations.SerializedName

data class ResendEmailResponse(

	@field:SerializedName("data")
	val data: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
