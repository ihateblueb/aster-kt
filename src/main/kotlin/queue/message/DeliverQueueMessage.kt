package site.remlit.blueb.queue.message

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class DeliverQueueMessage(
	val user: String,
	val inbox: String,
	val activity: JsonObject,
)
