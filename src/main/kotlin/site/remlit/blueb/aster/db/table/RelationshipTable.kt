package site.remlit.blueb.aster.db.table

import kotlinx.datetime.toJavaLocalDateTime
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.enum
import org.ktorm.schema.varchar
import site.remlit.blueb.aster.db.entity.RelationshipEntity
import site.remlit.blueb.aster.model.RelationshipType

/**
 * Table for storing user relationships.
 *
 * @since 2025.9.1.2-SNAPSHOT
 * */
object RelationshipTable : Table<RelationshipEntity>("relationship") {
	val id = varchar("id").primaryKey()
		.bindTo { it.id }

	val type = enum<RelationshipType>("type")
		.bindTo { it.type }

	val to = varchar("to")
		.references(UserTable) { it.to }
	val from = varchar("from")
		.references(UserTable) { it.from }

	val activityId = varchar("activityId")
		.bindTo { it.activityId }

	val createdAt = datetime("createdAt")
		.bindTo { it.createdAt.toJavaLocalDateTime() }
	val updatedAt = datetime("updatedAt")
		.bindTo { it.updatedAt?.toJavaLocalDateTime() }
}