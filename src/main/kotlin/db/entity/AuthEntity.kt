package me.blueb.db.entity

import me.blueb.db.table.AuthTable
import me.blueb.db.table.UserTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class AuthEntity(id: EntityID<String>) : Entity<String>(id) {
	companion object : EntityClass<String, AuthEntity>(AuthTable)

	var token by AuthTable.token
	var user by referencedOn(UserTable)
	var createdAt by AuthTable.createdAt
}
