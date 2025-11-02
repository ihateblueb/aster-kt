package site.remlit.aster.db.entity

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import site.remlit.aster.db.table.InstanceTable

class InstanceEntity(id: EntityID<String>) : Entity<String>(id) {
	companion object : EntityClass<String, InstanceEntity>(InstanceTable)

	val host by InstanceTable.host

	var name by InstanceTable.name
	var description by InstanceTable.description
	var color by InstanceTable.color
	var icon by InstanceTable.icon
	
	val createdAt by InstanceTable.createdAt
	var updatedAt by InstanceTable.updatedAt
}
