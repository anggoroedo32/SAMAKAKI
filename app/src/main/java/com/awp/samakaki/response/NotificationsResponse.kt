package com.awp.samakaki.response

import com.google.gson.annotations.SerializedName

data class NotificationsResponse(

	@field:SerializedName("data")
	val data: DataNotifications? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class UnreadItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("inviting_name")
	val invitingName: String? = null,

	@field:SerializedName("inviting_email")
	val invitingEmail: String? = null,

	@field:SerializedName("descriptions")
	val descriptions: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("relation")
	val relation: String? = null
)

data class ReadItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("inviting_name")
	val invitingName: String? = null,

	@field:SerializedName("inviting_email")
	val invitingEmail: String? = null,

	@field:SerializedName("descriptions")
	val descriptions: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("relation")
	val relation: String? = null
)

data class DataNotifications(

	@field:SerializedName("read")
	val read: List<ReadItem?>? = null,

	@field:SerializedName("unread")
	val unread: List<UnreadItem?>? = null
)
