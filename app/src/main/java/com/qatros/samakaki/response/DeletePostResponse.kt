package com.qatros.samakaki.response

import com.google.gson.annotations.SerializedName

data class DeletePostResponse(

	@field:SerializedName("data")
	val data: DataDeletePost? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataDeletePost(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("descriptions")
	val descriptions: String? = null,

	@field:SerializedName("user")
	val user: UserInformation? = null,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class UserInformation(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null
)
