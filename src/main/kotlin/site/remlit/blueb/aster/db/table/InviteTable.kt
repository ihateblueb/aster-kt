package site.remlit.blueb.aster.db.table

import kotlinx.datetime.toJavaLocalDateTime
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar
import site.remlit.blueb.aster.db.entity.InviteEntity

/**
 * Table to store invites to join the instance.
 *
 * @since 2025.9.1.2-SNAPSHOT
 * */
object InviteTable : Table<InviteEntity>("invite") {
	val id = varchar("id").primaryKey()
		.bindTo { it.id }
	
	val code = varchar("code")
		.bindTo { it.code }

	val user = varchar("user")
		.references(UserTable) { it.user }
	val creator = varchar("creator")
		.references(UserTable) { it.creator }

	val createdAt = datetime("createdAt")
		.bindTo { it.createdAt.toJavaLocalDateTime() }
	val usedAt = datetime("usedAt")
		.bindTo { it.usedAt?.toJavaLocalDateTime() }
}