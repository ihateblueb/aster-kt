package site.remlit.blueb.aster.db.table

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime
import site.remlit.blueb.aster.common.model.NotificationType

object NotificationTable : IdTable<String>("notification") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_notification_id").entityId()

	val type = enumeration("type", NotificationType::class)

	val to = reference("to", UserTable.id, onDelete = ReferenceOption.CASCADE)
	val from = reference("from", UserTable.id, onDelete = ReferenceOption.CASCADE)

	val note = reference("note", NoteTable.id, onDelete = ReferenceOption.CASCADE)
	val relationship = reference("relationship", RelationshipTable.id, onDelete = ReferenceOption.CASCADE)

	val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)

	override val primaryKey = PrimaryKey(id)
}

