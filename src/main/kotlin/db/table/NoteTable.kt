package me.blueb.db.table

import me.blueb.model.Visibility
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object NoteTable : IdTable<String>("note") {
    override val id = varchar("id", length = 125).uniqueIndex("unique_note_id").entityId()

    val apId = varchar("apId", length = 1025).uniqueIndex("unique_note_apId")
	val conversation = varchar("conversation", length = 1025).nullable()
	
	val user = reference("user", UserTable, onDelete = ReferenceOption.CASCADE)
	val replyingTo = reference("replyingTo", NoteTable, onDelete = ReferenceOption.CASCADE).nullable()

    val content = varchar("content", length = 25000).index("note_content_index")

	val visibility = enumeration("visibility", Visibility::class)
    val to = array<String>("to").index("note_to_index")
    val tags = array<String>("tags").index("note_tag_index")

    val createdAt = datetime("createdAt")
	val updatedAt = datetime("updatedAt").nullable()

    override val primaryKey = PrimaryKey(id)
}
