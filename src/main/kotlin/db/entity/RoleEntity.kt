package me.blueb.db.entity

import me.blueb.db.table.RoleTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID


class RoleEntity(id: EntityID<String>) : Entity<String>(id) {
	companion object : EntityClass<String, RoleEntity>(RoleTable)

	var type by RoleTable.type

	var name by RoleTable.name
	var description by RoleTable.description

	var createdAt by RoleTable.createdAt
	var updatedAt by RoleTable.updatedAt
}
