package site.remlit.aster.db.entity

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import site.remlit.aster.db.table.RelationshipTable

class RelationshipEntity(id: EntityID<String>) : Entity<String>(id) {
	companion object : EntityClass<String, RelationshipEntity>(RelationshipTable)

	var type by RelationshipTable.type

	var to by UserEntity referencedOn RelationshipTable.to
	var from by UserEntity referencedOn RelationshipTable.from

	var pending by RelationshipTable.pending
	var activityId by RelationshipTable.activityId

	val createdAt by RelationshipTable.createdAt
	var updatedAt by RelationshipTable.updatedAt
}
