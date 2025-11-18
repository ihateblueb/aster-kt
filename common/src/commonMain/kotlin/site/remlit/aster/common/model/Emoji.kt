package site.remlit.aster.common.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
@JsExport
data class Emoji(
	val id: String,
	val apId: String,

	val name: String,
	val host: String? = null,
	val src: String,

	val createdAt: LocalDateTime,
	val updatedAt: LocalDateTime? = null,
)
