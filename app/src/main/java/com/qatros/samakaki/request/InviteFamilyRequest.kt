package com.qatros.samakaki.request

import com.google.gson.annotations.SerializedName

data class InviteFamilyRequest(

	@field:SerializedName("token_invitation")
	val tokenInvitation: String? = null
)
