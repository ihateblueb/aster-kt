package site.remlit.blueb.aster.db.entity

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import site.remlit.blueb.aster.db.table.UserPrivateTable

class UserPrivateEntity(id: EntityID<String>) : Entity<String>(id) {
	companion object : EntityClass<String, UserPrivateEntity>(UserPrivateTable)

	var password by UserPrivateTable.password
	var privateKey by UserPrivateTable.privateKey
}
