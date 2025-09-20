package site.remlit.blueb.aster.db.table

import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar
import site.remlit.blueb.aster.db.entity.NoteLikeEntity

/**
 * Table for storing note likes.
 *
 * @since 2025.9.1.2-SNAPSHOT
 * */
object NoteLikeTable : Table<NoteLikeEntity>("note_like") {
	val id = varchar("id").primaryKey()

	val user = varchar("user")
		.references(UserTable) { it.user }
	val note = varchar("note")
		.references(NoteTable) { it.note }

	val createdAt = datetime("createdAt")
}