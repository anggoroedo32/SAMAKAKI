package com.awp.samakaki.response

import com.google.gson.annotations.SerializedName

data class PostsResponse(

	@field:SerializedName("data")
	val data: Data? = null
)

data class PostItem(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: Any? = null,

	@field:SerializedName("descriptions")
	val descriptions: Any? = null,

	@field:SerializedName("status")
	val status: Any? = null
)

data class Data(

	@field:SerializedName("post")
	val post: List<PostItem?>? = null
)
