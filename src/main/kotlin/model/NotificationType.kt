package site.remlit.blueb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class NotificationType {
	@SerialName("like")
	Like,

	@SerialName("react")
	React,

	@SerialName("repeat")
	Repeat,

	@SerialName("follow")
	Follow,

	@SerialName("acceptedFollow")
	AcceptedFollow,

	@SerialName("mention")
	Mention,

	@SerialName("announcement")
	Announcement,

	@SerialName("brokenRelationship")
	BrokenRelationship,

	// admin
	@SerialName("registration")
	Registration,

	@SerialName("report")
	Report
}
