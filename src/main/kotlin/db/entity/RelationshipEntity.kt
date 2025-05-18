package me.blueb.db.entity

import me.blueb.db.table.RelationshipTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RelationshipEntity(id: EntityID<String>) : Entity<String>(id) {
	companion object : EntityClass<String, RelationshipEntity>(RelationshipTable)

	var type by RelationshipTable.type

	var to by UserEntity referencedOn RelationshipTable.to
	var from by UserEntity referencedOn RelationshipTable.from

	var activityId by RelationshipTable.activityId

	var createdAt by RelationshipTable.createdAt
	var updatedAt by RelationshipTable.updatedAt
}
