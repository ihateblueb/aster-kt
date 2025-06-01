package site.remlit.blueb.db.entity

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import site.remlit.blueb.db.table.UserPrivateTable

class UserPrivateEntity(id: EntityID<String>) : Entity<String>(id) {
	companion object : EntityClass<String, UserPrivateEntity>(UserPrivateTable)

	var password by UserPrivateTable.password
	var privateKey by UserPrivateTable.privateKey
}
