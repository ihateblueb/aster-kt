package site.remlit.blueb.aster.db.table

import org.jetbrains.exposed.v1.core.dao.id.IdTable

object UserPrivateTable : IdTable<String>("user_private") {
	override val id = varchar("id", length = 125).uniqueIndex("unique_user_private_id").entityId()

	val password = varchar("password", length = 250)
	val privateKey = varchar("privateKey", length = 5000)

	override val primaryKey = PrimaryKey(NoteTable.id)
}
