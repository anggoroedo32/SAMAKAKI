package com.qatros.samakaki.response

import com.google.gson.annotations.SerializedName

data class CreateFamilyTreeResponse(

	@field:SerializedName("data")
	val data: DataCreateFamilyTree? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataCreateFamilyTree(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
