package site.remlit.blueb.aster.db.table

import kotlinx.datetime.toJavaLocalDateTime
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar
import site.remlit.blueb.aster.db.entity.AuthEntity

/**
 * Table for storing authentication tokens of registered users.
 *
 * @since 2025.9.1.2-SNAPSHOT
 * */
object AuthTable : Table<AuthEntity>("auth") {
	val id = varchar("id").primaryKey()
		.bindTo { it.id }

	val token = varchar("token")
		.bindTo { it.token }
	val user = varchar("user")
		.references(UserTable) { it.user }

	val createdAt = datetime("createdAt")
		.bindTo { it.createdAt.toJavaLocalDateTime() }
}