package site.remlit.aster.db.table

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

object DriveFileTable : IdTable<String>("drive_file") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_drive_file_id").entityId()

	val type = varchar("type", length = 125)
	val src = varchar("src", length = 5000)
	val alt = varchar("alt", length = 10000).nullable()

	val sensitive = bool("sensitive").default(false)

	val user = reference("user", UserTable.id, onDelete = ReferenceOption.CASCADE)

	val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)
	val updatedAt = datetime("updatedAt").nullable()

	override val primaryKey = PrimaryKey(id)
}
