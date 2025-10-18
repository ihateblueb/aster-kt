package site.remlit.blueb.aster.util.model

import site.remlit.blueb.aster.common.model.Relationship
import site.remlit.blueb.aster.common.model.User
import site.remlit.blueb.aster.db.entity.RelationshipEntity


fun Relationship.Companion.fromEntity(entity: RelationshipEntity) = Relationship(
	id = entity.id.toString(),

	type = entity.type,

	to = User.fromEntity(entity.to),
	from = User.fromEntity(entity.from),

	createdAt = entity.createdAt,
	updatedAt = entity.updatedAt
)

fun Relationship.Companion.fromEntities(entities: List<RelationshipEntity>): List<Relationship> =
	entities.map { fromEntity(it) }