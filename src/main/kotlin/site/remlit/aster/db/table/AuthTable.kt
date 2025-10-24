package site.remlit.aster.db.table

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

object AuthTable : IdTable<String>("auth") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_auth_id").entityId()

	val token = varchar("token", length = 275).uniqueIndex("unique_auth_token")
	val user = reference("user", UserTable.id, onDelete = ReferenceOption.CASCADE)

	val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)

	override val primaryKey = PrimaryKey(id)
}
