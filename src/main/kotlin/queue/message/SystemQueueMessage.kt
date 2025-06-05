package site.remlit.blueb.aster.queue.message

import kotlinx.serialization.Serializable

@Serializable
data class SystemQueueMessage(
	val type: SystemQueueMessageType,
	val host: String? = null
)
