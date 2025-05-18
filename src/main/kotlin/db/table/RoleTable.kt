package me.blueb.db.table

import me.blueb.model.RoleType
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object RoleTable : IdTable<String>("role") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_role_id").entityId()

	val type = enumeration("type", RoleType::class)

	val name = varchar("name", length = 500).uniqueIndex("unique_role_name")
	val description = varchar("description", length = 2750).nullable()

	val createdAt = datetime("createdAt")
	val updatedAt = datetime("updatedAt").nullable()

	override val primaryKey = PrimaryKey(PolicyTable.id)
}
