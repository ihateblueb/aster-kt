package site.remlit.blueb.aster.db.table

import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.enum
import org.ktorm.schema.varchar
import site.remlit.blueb.aster.db.entity.NotificationEntity
import site.remlit.blueb.aster.model.NotificationType

/**
 * Table for storing user notifications.
 *
 * @since 2025.9.1.2-SNAPSHOT
 * */
object NotificationTable : Table<NotificationEntity>("notification") {
	val id = varchar("id").primaryKey()
	val type = enum<NotificationType>("type")

	val to = varchar("to")
		.references(UserTable) { it.to }
	val from = varchar("from")
		.references(UserTable) { it.from }

	val note = varchar("note")
		.references(NoteTable) { it.note }

	val createdAt = datetime("createdAt")
}