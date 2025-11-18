package site.remlit.aster.db.table

import org.jetbrains.exposed.v1.core.dao.id.IdTable

object KeyValTable : IdTable<String>("keyval") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_keyval_id").entityId()

	val key = text("key", eagerLoading = true).uniqueIndex("unique_keyval_key")
	val value = text("value", eagerLoading = true).nullable()

	override val primaryKey = PrimaryKey(InviteTable.id)
}
