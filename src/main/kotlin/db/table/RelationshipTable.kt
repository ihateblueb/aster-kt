package site.remlit.blueb.aster.db.table

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime
import site.remlit.blueb.aster.model.RelationshipType

object RelationshipTable : IdTable<String>("relationship") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_relationship_id").entityId()

	val type = enumeration("type", RelationshipType::class)

	val to = reference("to", UserTable.id, onDelete = ReferenceOption.CASCADE)
	val from = reference("from", UserTable.id, onDelete = ReferenceOption.CASCADE)

	val activityId = varchar("activityId", length = 2750).nullable()

	val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)
	val updatedAt = datetime("updatedAt").nullable()

	override val primaryKey = PrimaryKey(PolicyTable.id)
}
