package com.awp.samakaki.response

import com.google.gson.annotations.SerializedName

data class UserRelationsResponse(

    @field:SerializedName("data")
    val data: DataUserRelation? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class DataUserRelation(

    @field:SerializedName("current_user")
    val currentUser: String? = null,

    @field:SerializedName("relation")
    val relation: List<RelationItem?>? = null
)

data class RelationItem(

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("user_related")
    val userRelated: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("relation_name")
    val relationName: String? = null
)