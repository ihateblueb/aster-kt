package site.remlit.blueb.aster.common.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Relationship(
	val id: String,

	val type: RelationshipType,

	val to: User,
	val from: User,

	val createdAt: LocalDateTime,
	val updatedAt: LocalDateTime? = null
)
