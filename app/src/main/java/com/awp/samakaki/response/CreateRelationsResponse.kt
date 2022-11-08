package com.awp.samakaki.response

import com.google.gson.annotations.SerializedName

data class CreateRelationsResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("user_relation")
	val userRelation: UserRelation? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("invitaion_token")
	val invitaionToken: String? = null,

	@field:SerializedName("relation_name")
	val relationName: String? = null
)

data class UserRelation(

	@field:SerializedName("connected_user_id")
	val connectedUserId: Any? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("relation_id")
	val relationId: Int? = null
)
