package site.remlit.aster.common.model.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
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

	@SerialName("bite")
	Bite,

	// admin
	@SerialName("registration")
	Registration,

	@SerialName("report")
	Report
}