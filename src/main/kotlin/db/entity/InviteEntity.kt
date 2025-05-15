package me.blueb.db.entity

import me.blueb.db.table.AuthTable
import me.blueb.db.table.InviteTable
import me.blueb.db.table.UserTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class InviteEntity(id: EntityID<String>) : Entity<String>(id) {
	companion object : EntityClass<String, InviteEntity>(InviteTable)

	var code by InviteTable.code

	var user by UserEntity referencedOn InviteTable.user
	var creator by UserEntity referencedOn InviteTable.user

	var createdAt by InviteTable.createdAt
}
