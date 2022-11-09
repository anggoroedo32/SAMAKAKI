package com.awp.samakaki.response

import com.google.gson.annotations.SerializedName

data class UpdateRelationsResponse(

	@field:SerializedName("data")
	val data: DataUpdate? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataUpdate(

	@field:SerializedName("relation")
	val relation: Relation? = null
)

data class Relation(

	@field:SerializedName("connected_user_id")
	val connectedUserId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("relation_id")
	val relationId: Int? = null
)
