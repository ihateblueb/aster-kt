package site.remlit.blueb.queue.message

import kotlinx.serialization.Serializable

@Serializable
data class SystemQueueMessage(
	val type: SystemQueueMessageType,
	val host: String? = null
)
