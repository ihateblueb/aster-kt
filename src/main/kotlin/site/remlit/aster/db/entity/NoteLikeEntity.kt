package site.remlit.aster.db.entity

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import site.remlit.aster.db.table.NoteLikeTable

class NoteLikeEntity(id: EntityID<String>) : Entity<String>(id) {
	companion object : EntityClass<String, NoteLikeEntity>(NoteLikeTable)

	var user by UserEntity referencedOn NoteLikeTable.user
	var note by NoteEntity referencedOn NoteLikeTable.note
	var createdAt by NoteLikeTable.createdAt
}

