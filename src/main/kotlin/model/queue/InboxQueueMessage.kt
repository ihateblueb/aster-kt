package me.blueb.model.queue

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class InboxQueueMessage(
	val from: String,
	val activity: JsonObject,
)
