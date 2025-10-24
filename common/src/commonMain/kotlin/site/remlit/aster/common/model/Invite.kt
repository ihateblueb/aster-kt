package site.remlit.aster.common.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class Invite(
	val id: String,

	val code: String,

	val user: site.remlit.aster.common.model.User? = null,
	val creator: site.remlit.aster.common.model.User,

	val createdAt: LocalDateTime,
	val usedAt: LocalDateTime? = null,
)