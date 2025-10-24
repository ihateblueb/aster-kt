package site.remlit.aster.common.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class Policy(
	val id: String,

	val type: site.remlit.aster.common.model.type.PolicyType,

	val host: String,
	val content: String? = null,

	val createdAt: LocalDateTime,
	val updatedAt: LocalDateTime? = null
)
