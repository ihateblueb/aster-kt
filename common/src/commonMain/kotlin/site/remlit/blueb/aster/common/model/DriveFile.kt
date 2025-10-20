package site.remlit.blueb.aster.common.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class DriveFile(
	val id: String,

	val type: String,
	val src: String,
	val alt: String?,

	val sensitive: Boolean,

	val user: User,

	val createdAt: LocalDateTime,
	val updatedAt: LocalDateTime?,
)
