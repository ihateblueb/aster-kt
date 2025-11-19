package site.remlit.aster.db.entity

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import site.remlit.aster.db.table.EmojiTable

class EmojiEntity(id: EntityID<String>) : Entity<String>(id) {
	companion object : EntityClass<String, EmojiEntity>(EmojiTable)

	var apId by EmojiTable.apId

	var name by EmojiTable.name
    var category by EmojiTable.category
	var host by EmojiTable.host
	var src by EmojiTable.src

	var createdAt by EmojiTable.createdAt
	var updatedAt by EmojiTable.updatedAt
}
