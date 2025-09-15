package site.remlit.blueb.aster.db.table

import org.ktorm.schema.Table
import org.ktorm.schema.timestamp
import org.ktorm.schema.varchar
import site.remlit.blueb.aster.db.entity.AuthEntity

/**
 * Table for storing authentication tokens of registered users.
 *
 * @since 2025.9.1.2-SNAPSHOT
 * */
object AuthTable : Table<AuthEntity>("auth") {
	val id = varchar("id").primaryKey()
	val token = varchar("token")
	val user = varchar("user")
		.references(UserTable) { it.user }
	val createdAt = timestamp("createdAt")
}