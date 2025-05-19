package me.blueb.db.table

import me.blueb.model.NotificationType
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object NotificationTable : IdTable<String>("notification") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_notification_id").entityId()

	val type = enumeration("type", NotificationType::class)

	val to = reference("to", UserTable, onDelete = ReferenceOption.CASCADE)
	val from = reference("from", UserTable, onDelete = ReferenceOption.CASCADE)

	val note = reference("note", NoteTable, onDelete = ReferenceOption.CASCADE)
	val relationship = reference("relationship", RelationshipTable, onDelete = ReferenceOption.CASCADE)

	val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)

	override val primaryKey = PrimaryKey(id)
}

