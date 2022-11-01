package com.awp.samakaki.response

import com.google.gson.annotations.SerializedName

data class UserRelationsResponse(

	@field:SerializedName("data")
	val data: DataUserRelations,

	@field:SerializedName("status")
	val status: String? = null
)

data class UserRelation(

	@field:SerializedName("connected_user_id")
	val connectedUserId: Int? = null,

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

data class RelationDetail(

	@field:SerializedName("user_relation")
	val userRelation: UserRelation? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("relation")
	val relation: Relation? = null
)

data class DataUserRelations(

	@field:SerializedName("relation_detail")
	val relationDetail: List<RelationDetailItem?>? = null
)

data class User(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("reset_password_sent_at")
	val resetPasswordSentAt: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("password_digest")
	val passwordDigest: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("reset_password_token")
	val resetPasswordToken: Any? = null
)

data class RelationDetailItem(

	@field:SerializedName("relation_detail")
	val relationDetail: RelationDetail? = null
)

data class Relation(

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
