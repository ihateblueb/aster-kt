package me.blueb.model.queue

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class SystemQueueMessage(
	val type: SystemQueueMessageType,
	val host: String? = null
)
