package site.remlit.blueb.aster.db.entity

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import site.remlit.blueb.aster.db.table.InviteTable

class InviteEntity(id: EntityID<String>) : Entity<String>(id) {
	companion object : EntityClass<String, InviteEntity>(InviteTable)

	var code by InviteTable.code

	var user by UserEntity optionalReferencedOn InviteTable.user
	var creator by UserEntity referencedOn InviteTable.creator

	var createdAt by InviteTable.createdAt
	var usedAt by InviteTable.usedAt
}
