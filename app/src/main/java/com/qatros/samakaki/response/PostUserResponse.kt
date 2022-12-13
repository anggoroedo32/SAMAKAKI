package com.qatros.samakaki.response

import com.google.gson.annotations.SerializedName

data class PostUserResponse(

	@field:SerializedName("data")
	val data: PostsData? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class UserDataPosts(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null
)

data class PostsData(

	@field:SerializedName("posts")
	val posts: List<ItemPosts?>? = null
)

data class ItemPosts(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("descriptions")
	val descriptions: String? = null,

	@field:SerializedName("user")
	val user: UserDataPosts? = null,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
