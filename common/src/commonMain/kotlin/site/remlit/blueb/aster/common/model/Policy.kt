package site.remlit.blueb.aster.common.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import site.remlit.blueb.aster.common.model.type.PolicyType
import kotlin.js.JsExport

@JsExport
@Serializable
data class Policy(
	val id: String,

	val type: PolicyType,

	val host: String,
	val content: String? = null,

	val createdAt: LocalDateTime,
	val updatedAt: LocalDateTime? = null
)
