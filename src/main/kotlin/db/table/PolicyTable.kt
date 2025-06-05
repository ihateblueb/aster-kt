package site.remlit.blueb.aster.db.table

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import site.remlit.blueb.aster.model.PolicyType

object PolicyTable : IdTable<String>("policy") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_policy_id").entityId()

	val type = enumeration("type", PolicyType::class)

	val host = varchar("host", length = 2750)
	val content = varchar("content", length = 5000).nullable()

	val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)
	val updatedAt = datetime("updatedAt").nullable()

	override val primaryKey = PrimaryKey(id)
}
