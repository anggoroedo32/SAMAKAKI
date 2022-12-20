package com.qatros.samakaki.response

import com.google.gson.annotations.SerializedName

data class SingleMessageResponse(

	@field:SerializedName("message")
	val message: String? = null
)
