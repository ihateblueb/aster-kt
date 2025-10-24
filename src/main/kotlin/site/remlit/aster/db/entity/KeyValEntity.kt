package site.remlit.aster.db.entity

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import site.remlit.aster.db.table.KeyValTable

class KeyValEntity(id: EntityID<String>) : Entity<String>(id) {
	companion object : EntityClass<String, KeyValEntity>(KeyValTable)

	var key by KeyValTable.key
	var value by KeyValTable.value
}
