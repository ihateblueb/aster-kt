package site.remlit.blueb.aster.db.table

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import site.remlit.blueb.aster.model.RoleType

object RoleTable : IdTable<String>("role") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_role_id").entityId()

	val type = enumeration("type", RoleType::class)

	val name = varchar("name", length = 500).uniqueIndex("unique_role_name")
	val description = varchar("description", length = 2750).nullable()

	val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)
	val updatedAt = datetime("updatedAt").nullable()

	override val primaryKey = PrimaryKey(PolicyTable.id)
}
