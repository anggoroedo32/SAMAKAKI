package com.awp.samakaki.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PostsResponse(

	@field:SerializedName("data")
	val data: DataPosts? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("posts")
	val posts: List<PostsItem>
)

@Parcelize
data class PostsItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("descriptions")
	val descriptions: String? = null,

	@field:SerializedName("user")
	val user: UserPosts? = null,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

data class DataPosts(

	@field:SerializedName("posts")
	val posts: List<PostsItem?>? = null
)

@Parcelize
data class UserPosts(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null
): Parcelable
