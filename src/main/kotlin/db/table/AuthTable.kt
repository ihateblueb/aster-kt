package me.blueb.db.table

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object AuthTable : IdTable<String>("auth") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_auth_id").entityId()

	val token = varchar("token", length = 275).uniqueIndex("unique_auth_token")
	val user = varchar("user", length = 125).references(UserTable.id, onDelete = ReferenceOption.CASCADE)
	val createdAt = datetime("createdAt")

	override val primaryKey = PrimaryKey(id)
}
