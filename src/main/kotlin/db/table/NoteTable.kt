package site.remlit.blueb.aster.db.table

import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.enum
import org.ktorm.schema.varchar
import site.remlit.blueb.aster.db.entity.NoteEntity
import site.remlit.blueb.aster.model.Visibility

/**
 * Table for storing notes.
 *
 * @since 2025.9.1.2-SNAPSHOT
 * */
object NoteTable : Table<NoteEntity>("note") {
	val id = varchar("id").primaryKey()

	val apId = varchar("apId")
	val conversation = varchar("conversation")

	val user = varchar("user")
		.references(UserTable) { it.user }
	val replyingTo = varchar("replyingTo")
		.references(NoteTable) { it.replyingTo }

	val cw = varchar("cw")
	val content = varchar("content")

	val visibility = enum<Visibility>("visibility")

	val repeat = varchar("repeat")
		.references(NoteTable) { it.repeat }

	val createdAt = datetime("createdAt")
	val updatedAt = datetime("updatedAt")
}