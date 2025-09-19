package site.remlit.blueb.aster.db.table

import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.enum
import org.ktorm.schema.varchar
import site.remlit.blueb.aster.db.entity.RoleEntity
import site.remlit.blueb.aster.model.RoleType

object RoleTable : Table<RoleEntity>("role") {
	val id = varchar("id").primaryKey()

	val type = enum<RoleType>("type")

	val name = varchar("name")
	val description = varchar("description")

	val createdAt = datetime("created_at")
	val updatedAt = datetime("updatedAt")
}