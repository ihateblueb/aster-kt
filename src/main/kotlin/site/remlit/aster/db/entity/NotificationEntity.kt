package site.remlit.aster.db.entity

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import site.remlit.aster.db.table.NotificationTable

class NotificationEntity(id: EntityID<String>) : Entity<String>(id) {
	companion object : EntityClass<String, NotificationEntity>(NotificationTable)

	var type by NotificationTable.type

	var to by UserEntity referencedOn NotificationTable.to
	var from by UserEntity referencedOn NotificationTable.from

	var note by NoteEntity optionalReferencedOn NotificationTable.note
	var relationship by RelationshipEntity optionalReferencedOn NotificationTable.relationship

	var createdAt by NotificationTable.createdAt
}

