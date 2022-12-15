package com.qatros.samakaki.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("data")
	val data: DataRegister? = null,

	@field:SerializedName("status")
	val status: String? = null

)

data class DataRegister(

	@field:SerializedName("user")
	val userRegister: UserRegister? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class UserRegister(

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

data class MessageResponse(

	@field:SerializedName("message")
	val message: Message? = null
)

data class Message(

	@field:SerializedName("email")
	val email: List<String>? = null
)