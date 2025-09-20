package site.remlit.blueb.aster.db.table

import kotlinx.datetime.toJavaLocalDateTime
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.enum
import org.ktorm.schema.varchar
import site.remlit.blueb.aster.db.entity.RoleEntity
import site.remlit.blueb.aster.model.RoleType

/**
 * Table for storing user roles.
 *
 * @since 2025.9.1.2-SNAPSHOT
 * */
object RoleTable : Table<RoleEntity>("role") {
	val id = varchar("id").primaryKey()
		.bindTo { it.id }

	val type = enum<RoleType>("type")
		.bindTo { it.type }

	val name = varchar("name")
		.bindTo { it.name }
	val description = varchar("description")
		.bindTo { it.description }

	val createdAt = datetime("createdAt")
		.bindTo { it.createdAt.toJavaLocalDateTime() }
	val updatedAt = datetime("updatedAt")
		.bindTo { it.updatedAt?.toJavaLocalDateTime() }
}