package site.remlit.aster.common.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class Relationship(
	val id: String,

	val type: site.remlit.aster.common.model.type.RelationshipType,

	val to: site.remlit.aster.common.model.User,
	val from: site.remlit.aster.common.model.User,

	val createdAt: LocalDateTime,
	val updatedAt: LocalDateTime? = null
)
