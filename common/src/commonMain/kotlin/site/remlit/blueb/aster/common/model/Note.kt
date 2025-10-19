package site.remlit.blueb.aster.common.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class Note(
	val id: String,

	val apId: String,
	val conversation: String? = null,

	val user: User,
	val replyingTo: Note? = null,

	val cw: String? = null,
	val content: String,

	val visibility: Visibility,
	val to: List<String>? = null,
	val tags: List<String>? = null,

	val repeat: Note? = null,

	val createdAt: LocalDateTime,
	val updatedAt: LocalDateTime? = null,

	val likes: List<SmallUser> = emptyList(),
	val reactions: List<SmallUser> = emptyList(),
	val repeats: List<Note> = emptyList()
)