package site.remlit.blueb.aster.db.table

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object NoteLikeTable : IdTable<String>("note_like") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_note_like_id").entityId()

	val user = reference("user", UserTable, onDelete = ReferenceOption.CASCADE)
	val note = reference("note", NoteTable, onDelete = ReferenceOption.CASCADE)
	val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)

	override val primaryKey = PrimaryKey(id)
}

