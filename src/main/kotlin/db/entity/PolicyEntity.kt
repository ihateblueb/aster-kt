package me.blueb.db.entity

import me.blueb.db.table.PolicyTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PolicyEntity(id: EntityID<String>) : Entity<String>(id) {
	companion object : EntityClass<String, PolicyEntity>(PolicyTable)

	var type by PolicyTable.type

	var host by PolicyTable.host
	var content by PolicyTable.content

	var createdAt by PolicyTable.createdAt
	var updatedAt by PolicyTable.updatedAt
}
