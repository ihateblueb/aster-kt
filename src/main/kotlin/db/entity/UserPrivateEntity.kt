package me.blueb.db.entity

import me.blueb.db.table.UserPrivateTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserPrivateEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, UserPrivateEntity>(UserPrivateTable)

    var password by UserPrivateTable.password
	var privateKey by UserPrivateTable.privateKey
}
