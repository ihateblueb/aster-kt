package me.blueb.db.table

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.date

object InviteTable : IdTable<String>("invite") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_invite_id").entityId()

	val code = varchar("code", length = 275).uniqueIndex("unique_invite_code")

	val user = varchar("user", length = 125).references(UserTable.id, onDelete = ReferenceOption.CASCADE)
	val creator = varchar("creator", length = 125).references(UserTable.id, onDelete = ReferenceOption.CASCADE)

	val createdAt = date("createdAt")

	override val primaryKey = PrimaryKey(id)
}
