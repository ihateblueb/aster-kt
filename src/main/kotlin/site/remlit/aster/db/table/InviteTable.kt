package site.remlit.aster.db.table

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

object InviteTable : IdTable<String>("invite") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_invite_id").entityId()

	val code = varchar("code", length = 275).uniqueIndex("unique_invite_code")

	val user = optReference("user", UserTable.id, onDelete = ReferenceOption.CASCADE)
	val creator = reference("creator", UserTable.id, onDelete = ReferenceOption.CASCADE)

	val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)
	val usedAt = datetime("usedAt").nullable()

	override val primaryKey = PrimaryKey(id)
}
