package site.remlit.aster.db.entity

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import site.remlit.aster.db.table.InstanceTable

class InstanceEntity(id: EntityID<String>) : Entity<String>(id) {
	companion object : EntityClass<String, InstanceEntity>(InstanceTable)

	var host by InstanceTable.host

	var name by InstanceTable.name
	var description by InstanceTable.description
	var color by InstanceTable.color
	var icon by InstanceTable.icon

	var software by InstanceTable.software
	var version by InstanceTable.version
	var contact by InstanceTable.contact

	val createdAt by InstanceTable.createdAt
	var updatedAt by InstanceTable.updatedAt
}
