package site.remlit.blueb.aster.db.entity

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import site.remlit.blueb.aster.db.table.InboxQueueTable

class InboxQueueEntity(id: EntityID<String>) : Entity<String>(id = id) {
	companion object : EntityClass<String, InboxQueueEntity>(InboxQueueTable)

	val status by InboxQueueTable.status

	val content by InboxQueueTable.content
	val sender by InboxQueueTable.sender

	val createdAt by InboxQueueTable.createdAt
	var retries by InboxQueueTable.retries
}