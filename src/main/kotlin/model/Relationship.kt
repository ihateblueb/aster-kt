package site.remlit.blueb.aster.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import site.remlit.blueb.aster.db.entity.RelationshipEntity

@Serializable
data class Relationship(
	val id: String,

	val type: RelationshipType,

	val to: User,
	val from: User,

	val createdAt: LocalDateTime,
	val updatedAt: LocalDateTime? = null
) {
	companion object {
		fun fromEntity(entity: RelationshipEntity) = Relationship(
			id = entity.id.toString(),

			type = entity.type,

			to = User.fromEntity(entity.to),
			from = User.fromEntity(entity.from),

			createdAt = entity.createdAt,
			updatedAt = entity.updatedAt
		)
	}
}
