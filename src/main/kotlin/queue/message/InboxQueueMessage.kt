package site.remlit.blueb.queue.message

import kotlinx.serialization.Serializable

@Serializable
data class InboxQueueMessage(
	val from: String,
	val activity: String,
)
