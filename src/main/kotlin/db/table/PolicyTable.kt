package me.blueb.db.table

import me.blueb.model.PolicyType
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object PolicyTable : IdTable<String>("policy") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_policy_id").entityId()

	val type = enumeration("type", PolicyType::class)

	val host = varchar("host", length = 2750)
	val content = varchar("content", length = 5000).nullable()

	val createdAt = datetime("createdAt")
	val updatedAt = datetime("updatedAt").nullable()

	override val primaryKey = PrimaryKey(id)
}
