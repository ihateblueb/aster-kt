package site.remlit.blueb.aster.db.table

import org.ktorm.schema.Table
import org.ktorm.schema.varchar
import site.remlit.blueb.aster.db.entity.UserPrivateEntity

/**
 * Table for storing local users password hashes and private keys for federation.
 *
 * @since 2025.9.1.2-SNAPSHOT
 * */
object UserPrivateTable : Table<UserPrivateEntity>("user_private") {
	var id = varchar("id").primaryKey()
		.bindTo { it.id }

	var password = varchar("password")
		.bindTo { it.password }
	var privateKey = varchar("privateKey")
		.bindTo { it.privateKey }
}