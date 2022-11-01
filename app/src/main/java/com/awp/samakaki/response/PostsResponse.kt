package com.awp.samakaki.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PostsResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("post")
	val post: List<PostItem>? = null
)

@Parcelize
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
	val title: String? = null,

	@field:SerializedName("descriptions")
	val descriptions: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("link")
	val link: String? = null
) : Parcelable

data class DataPosts(

	@field:SerializedName("post")
	val post: List<PostItem>? = null
)
