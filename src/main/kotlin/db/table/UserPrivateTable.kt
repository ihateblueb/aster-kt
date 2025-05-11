package me.blueb.db.table

import org.jetbrains.exposed.dao.id.IdTable

object UserPrivateTable : IdTable<String>("user_private") {
    override val id = varchar("id", length = 125).uniqueIndex("unique_user_private_id").entityId()
    val password = varchar("password", length = 250)

    override val primaryKey = PrimaryKey(NoteTable.id)
}