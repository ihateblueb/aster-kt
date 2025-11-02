package site.remlit.aster.db.table

import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

object InstanceTable : IdTable<String>("instance") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_instance_id").entityId()

	val host = varchar("host", length = 10000).uniqueIndex("unique_instance_host")

	val name = varchar("name", length = 1000).nullable()
	val description = varchar("description", length = 10000).nullable()
	val color = varchar("color", length = 6).default("000000")
	val icon = varchar("icon", length = 1000).nullable()

	val software = varchar("software", length = 1000).nullable()
	val version = varchar("version", length = 1000).nullable()
	val contact = varchar("contact", length = 1000).nullable()

	val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)
	val updatedAt = datetime("updatedAt").nullable()

	override val primaryKey = PrimaryKey(id)
}
