package com.qatros.samakaki.response

import com.google.gson.annotations.SerializedName

data class RegisterWithInvitationResponse(

	@field:SerializedName("data")
	val data: DataInvitation? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataInvitation(

	@field:SerializedName("inviting_user")
	val invitingUser: InvitingUser? = null,

	@field:SerializedName("token_login")
	val tokenLogin: String? = null,

	@field:SerializedName("token_invitation")
	val tokenInvitation: String? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("relation")
	val relation: String? = null
)

data class InvitingUser(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class User(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)
