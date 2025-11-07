package site.remlit.aster.common.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class SmallNote(
	val id: String,
	val apId: String,

	val user: SmallUser,

	val cw: String? = null,
	val content: String? = null,

	val visibility: Visibility,
	val to: List<String>? = null,
	val tags: List<String>? = null,

	val createdAt: LocalDateTime,
	val updatedAt: LocalDateTime? = null,
)
