package site.remlit.aster.db.table

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

object EmojiTable : IdTable<String>("emoji") {
	override val id: Column<EntityID<String>> = varchar("id", length = 125).uniqueIndex("unique_emoji_id").entityId()

	val apId = varchar("apId", length = 1025).uniqueIndex("unique_emoji_apId")

	val name = varchar("name", length = 250)
	val host = varchar("host", length = 500).nullable()
	val src = varchar("src", length = 1025)

	val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)
	val updatedAt = datetime("updatedAt").nullable()

	override val primaryKey = PrimaryKey(id)
}
