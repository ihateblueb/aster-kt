package site.remlit.blueb.db.table

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object InviteTable : IdTable<String>("invite") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_invite_id").entityId()

	val code = varchar("code", length = 275).uniqueIndex("unique_invite_code")

	val user = optReference("user", UserTable, onDelete = ReferenceOption.CASCADE)
	val creator = reference("creator", UserTable, onDelete = ReferenceOption.CASCADE)

	val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)
	val usedAt = datetime("usedAt").nullable()

	override val primaryKey = PrimaryKey(id)
}
