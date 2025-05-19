package me.blueb.model

import kotlinx.serialization.Serializable

@Serializable
enum class NotificationType {
	Like,
	React,
	Repeat,
	Follow,
	AcceptFollow,
	Mention,
	Announcement,
	BrokenRelationship,

	// admin
	Registration,
	Report
}
