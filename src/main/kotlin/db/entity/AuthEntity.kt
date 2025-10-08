package site.remlit.blueb.aster.db.entity

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import site.remlit.blueb.aster.db.table.AuthTable

class AuthEntity(id: EntityID<String>) : Entity<String>(id = id) {
	companion object : EntityClass<String, AuthEntity>(AuthTable)

	var token by AuthTable.token
	var user by UserEntity referencedOn AuthTable.user
	var createdAt by AuthTable.createdAt
}
