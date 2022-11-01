package com.awp.samakaki.response

import com.google.gson.annotations.SerializedName

data class CreateUserRelationsResponse(

	@field:SerializedName("data")
	val data: CreateDataUserRelation? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class CreateRelation(

	@field:SerializedName("data")
	val data: CreateDataUserRelation? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("number")
	val number: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("position")
	val position: String? = null,

	@field:SerializedName("relation_name")
	val relationName: String? = null
)

data class UserRelationItem(

	@field:SerializedName("connected_user_id")
	val connectedUserId: Any? = null,

	@field:SerializedName("family_tree_id")
	val familyTreeId: Int? = null,

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

data class CreateDataUserRelation(

	@field:SerializedName("user_relation")
	val userRelation: List<UserRelationItem?>? = null,

	@field:SerializedName("relation")
	val relation: CreateRelation? = null,

	@field:SerializedName("invitaion_token")
	val invitaionToken: String? = null
)
