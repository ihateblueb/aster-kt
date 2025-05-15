package me.blueb.db.table

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object NoteTable : IdTable<String>("note") {
    override val id = varchar("id", length = 125).uniqueIndex("unique_note_id").entityId()

    val apId = varchar("apId", length = 1025).uniqueIndex("unique_note_apId")
	val user = reference("user", UserTable, onDelete = ReferenceOption.CASCADE)

    val content = varchar("content", length = 25000).index("note_content_index")

    val to = array<String>("to").index("note_to_index")
    val tags = array<String>("tags").index("note_tag_index")

    val createdAt = datetime("createdAt")
	val updatedAt = datetime("updatedAt").nullable()

    override val primaryKey = PrimaryKey(id)
}
