package site.remlit.blueb.aster.db.table

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
	val type = enum<PolicyType>("type")

	val host = varchar("host")
	val content = varchar("content")

	val createdAt = datetime("createdAt")
	val updatedAt = datetime("updatedAt")
}