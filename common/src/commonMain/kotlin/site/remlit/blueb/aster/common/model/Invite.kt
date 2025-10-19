package site.remlit.blueb.aster.common.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class Invite(
	val id: String,

	val code: String,

	val user: User? = null,
	val creator: User,

	val createdAt: LocalDateTime,
	val usedAt: LocalDateTime? = null,
)