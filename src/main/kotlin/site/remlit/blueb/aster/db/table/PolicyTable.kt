package site.remlit.blueb.aster.db.table

import kotlinx.datetime.toJavaLocalDateTime
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.enum
import org.ktorm.schema.varchar
import site.remlit.blueb.aster.db.entity.PolicyEntity
import site.remlit.blueb.aster.model.PolicyType

/**
 * Table for storing moderation policies.
 *
 * @since 2025.9.1.2-SNAPSHOT
 * */
object PolicyTable : Table<PolicyEntity>("policy") {
	val id = varchar("id").primaryKey()
		.bindTo { it.id }

	val type = enum<PolicyType>("type")
		.bindTo { it.type }

	val host = varchar("host")
		.bindTo { it.host }
	val content = varchar("content")
		.bindTo { it.content }

	val createdAt = datetime("createdAt")
		.bindTo { it.createdAt.toJavaLocalDateTime() }
	val updatedAt = datetime("updatedAt")
		.bindTo { it.updatedAt?.toJavaLocalDateTime() }
}