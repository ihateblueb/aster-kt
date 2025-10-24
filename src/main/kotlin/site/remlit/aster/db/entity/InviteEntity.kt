package site.remlit.aster.db.entity

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import site.remlit.aster.db.table.InviteTable

class InviteEntity(id: EntityID<String>) : Entity<String>(id) {
	companion object : EntityClass<String, InviteEntity>(InviteTable)

	var code by InviteTable.code

	var user by UserEntity optionalReferencedOn InviteTable.user
	var creator by UserEntity referencedOn InviteTable.creator

	var createdAt by InviteTable.createdAt
	var usedAt by InviteTable.usedAt
}
