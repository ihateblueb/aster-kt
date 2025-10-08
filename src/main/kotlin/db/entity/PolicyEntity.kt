package site.remlit.blueb.aster.db.entity

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import site.remlit.blueb.aster.db.table.PolicyTable

class PolicyEntity(
	id:
	EntityID<String>
) : Entity<String>(id) {
	companion object : EntityClass<String, PolicyEntity>(PolicyTable)

	var type by PolicyTable.type

	var host by PolicyTable.host
	var content by PolicyTable.content

	var createdAt by PolicyTable.createdAt
	var updatedAt by PolicyTable.updatedAt
}
