package com.qatros.samakaki.response

import com.google.gson.annotations.SerializedName

data class InviteFamilyResponse(

	@field:SerializedName("data")
	val data: DataInviteFamily? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataInviteFamily(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("user_relation_id")
	val userRelationId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("descriptions")
	val descriptions: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
