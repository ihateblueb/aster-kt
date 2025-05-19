package me.blueb.db.table

import me.blueb.model.RelationshipType
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object RelationshipTable : IdTable<String>("relationship") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_relationship_id").entityId()

	val type = enumeration("type", RelationshipType::class)

	val to = reference("to", UserTable, onDelete = ReferenceOption.CASCADE)
	val from = reference("from", UserTable, onDelete = ReferenceOption.CASCADE)

	val activityId = varchar("activityId", length = 2750).nullable()

	val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)
	val updatedAt = datetime("updatedAt").nullable()

	override val primaryKey = PrimaryKey(PolicyTable.id)
}
