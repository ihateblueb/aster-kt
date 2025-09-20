package site.remlit.blueb.aster.db.table

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
	val type = enum<RelationshipType>("type")

	val to = varchar("to")
		.references(UserTable) { it.to }
	val from = varchar("from")
		.references(UserTable) { it.from }

	val activityId = varchar("activityId")

	val createdAt = datetime("createdAt")
	val updatedAt = datetime("updatedAt")
}