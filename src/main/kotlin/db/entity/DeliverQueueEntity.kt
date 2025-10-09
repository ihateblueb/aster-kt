package site.remlit.blueb.aster.db.entity

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import site.remlit.blueb.aster.db.table.DeliverQueueTable

class DeliverQueueEntity(id: EntityID<String>) : Entity<String>(id = id) {
	companion object : EntityClass<String, DeliverQueueEntity>(DeliverQueueTable)

	val status by DeliverQueueTable.status

	val content by DeliverQueueTable.content
	val sender by DeliverQueueTable.sender
	val inbox by DeliverQueueTable.inbox

	val createdAt by DeliverQueueTable.createdAt
	var retries by DeliverQueueTable.retries
}