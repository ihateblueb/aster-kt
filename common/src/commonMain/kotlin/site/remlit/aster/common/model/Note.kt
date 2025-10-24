package site.remlit.aster.common.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class Note(
	val id: String,

	val apId: String,
	val conversation: String? = null,

	val user: site.remlit.aster.common.model.User,
	val replyingTo: site.remlit.aster.common.model.Note? = null,

	val cw: String? = null,
	val content: String,

	val visibility: site.remlit.aster.common.model.Visibility,
	val to: List<String>? = null,
	val tags: List<String>? = null,

	val repeat: site.remlit.aster.common.model.Note? = null,

	val createdAt: LocalDateTime,
	val updatedAt: LocalDateTime? = null,

	val likes: List<site.remlit.aster.common.model.SmallUser> = emptyList(),
	val reactions: List<site.remlit.aster.common.model.SmallUser> = emptyList(),
	val repeats: List<site.remlit.aster.common.model.Note> = emptyList()
)