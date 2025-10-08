package site.remlit.blueb.aster.db.table

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

object NoteLikeTable : IdTable<String>("note_like") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_note_like_id").entityId()

	val user = reference("user", UserTable.id, onDelete = ReferenceOption.CASCADE)
	val note = reference("note", NoteTable.id, onDelete = ReferenceOption.CASCADE)
	val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)

	override val primaryKey = PrimaryKey(id)
}

