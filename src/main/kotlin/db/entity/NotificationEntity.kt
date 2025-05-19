package me.blueb.db.entity

import me.blueb.db.table.InviteTable
import me.blueb.db.table.NotificationTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class NotificationEntity(id: EntityID<String>) : Entity<String>(id) {
	companion object : EntityClass<String, NotificationEntity>(NotificationTable)

	var type by NotificationTable.type

	var to by UserEntity referencedOn NotificationTable.to
	var from by UserEntity referencedOn NotificationTable.from

	var note by NoteEntity referencedOn NotificationTable.note
	var relationship by RelationshipEntity referencedOn NotificationTable.relationship

	var createdAt by NotificationTable.createdAt
}

