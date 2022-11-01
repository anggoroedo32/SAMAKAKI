package com.awp.samakaki.response

import com.google.gson.annotations.SerializedName

data class GetUserRelationResponse(

	@field:SerializedName("data")
	val data: DataUserRelation? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataUserRelation(

	@field:SerializedName("connected_user_relationship")
	val connectedUserRelationship: List<ConnectedUserRelationshipItem>? = null,

	@field:SerializedName("current_user")
	val currentUser: CurrentUser? = null,

	@field:SerializedName("relation")
	val relation: List<RelationItem>? = null
)

data class ConnectedUserRelationshipItem(

	@field:SerializedName("connected_user")
	val connectedUser: Any? = null
)

data class CurrentUser(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("reset_password_sent_at")
	val resetPasswordSentAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("password_digest")
	val passwordDigest: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("reset_password_token")
	val resetPasswordToken: String? = null
)

data class RelationItem(

	@field:SerializedName("connected_user_id")
	val connectedUserId: Any? = null,

	@field:SerializedName("number")
	val number: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("family_tree")
	val familyTree: String? = null,

	@field:SerializedName("user_relation_id")
	val userRelationId: Int? = null,

	@field:SerializedName("position")
	val position: String? = null,

	@field:SerializedName("relation_name")
	val relationName: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("relation")
	val relation: String? = null
)
