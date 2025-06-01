package site.remlit.blueb.model.queue

import kotlinx.serialization.Serializable

@Serializable
data class SystemQueueMessage(
	val type: SystemQueueMessageType,
	val host: String? = null
)
