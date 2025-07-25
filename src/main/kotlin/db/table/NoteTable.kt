package site.remlit.blueb.aster.db.table

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import site.remlit.blueb.aster.model.Visibility

object NoteTable : IdTable<String>("note") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_note_id").entityId()

	val apId = varchar("apId", length = 1025).uniqueIndex("unique_note_apId")
	val conversation = varchar("conversation", length = 1025).nullable()

	val user = reference("user", UserTable, onDelete = ReferenceOption.CASCADE)
	val replyingTo = optReference("replyingTo", NoteTable, onDelete = ReferenceOption.CASCADE)

	val cw = varchar("cw", length = 5000).nullable().index("note_cw_index")
	val content = varchar("content", length = 25000).index("note_content_index")

	val visibility = enumeration("visibility", Visibility::class)

	val to = array<String>("to").default(listOf())
	val tags = array<String>("tags").default(listOf())
	val emojis = array<String>("emojis").default(listOf())

	val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)
	val updatedAt = datetime("updatedAt").nullable()

	override val primaryKey = PrimaryKey(id)
}
