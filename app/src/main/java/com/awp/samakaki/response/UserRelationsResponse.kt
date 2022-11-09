package com.awp.samakaki.response

import com.google.gson.annotations.SerializedName

data class UserRelationsResponse(

    @field:SerializedName("data")
    val data: DataRelationUser? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class DataRelationUser(

    @field:SerializedName("connected_user_relationship")
    val connectedUserRelationship: List<ConnectedUserRelationshipItem?>? = null,

    @field:SerializedName("current_user")
    val currentUser: CurrentUser? = null,

    @field:SerializedName("relation")
    val relation: List<RelationItem>
)

data class ConnectedUserRelationshipItem(

    @field:SerializedName("connected_user")
    val connectedUser: ConnectedUser? = null,

    @field:SerializedName("connected_user_relationship")
    val connectedUserRelationship: List<Any?>? = null,

    @field:SerializedName("connected_user_id")
    val connectedUserId: Int? = null,

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("user_related_id")
    val userRelatedId: Int? = null,

    @field:SerializedName("user_relation_id")
    val userRelationId: Int? = null,

    @field:SerializedName("user_related")
    val userRelated: String? = null,

    @field:SerializedName("avatar")
    val avatar: String? = null,

    @field:SerializedName("relation_name")
    val relationName: String? = null,

    @field:SerializedName("relation_id")
    val relationId: Int? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class CurrentUser(

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

data class ConnectedUser(

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

data class RelationItem(

    @field:SerializedName("connected_user_id")
    val connectedUserId: Int? = null,

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("user_related_id")
    val userRelatedId: Int? = null,

    @field:SerializedName("user_relation_id")
    val userRelationId: Int? = null,

    @field:SerializedName("user_related")
    val userRelated: String? = null,

    @field:SerializedName("avatar")
    val avatar: String? = null,

    @field:SerializedName("relation_name")
    val relationName: String? = null,

    @field:SerializedName("relation_id")
    val relationId: Int? = null,

    @field:SerializedName("status")
    val status: String? = null
)