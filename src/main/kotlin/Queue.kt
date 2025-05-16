package me.blueb

import io.ktor.server.application.*
import kolbasa.queue.PredefinedDataTypes
import kolbasa.queue.Queue
import kolbasa.schema.SchemaHelpers
import me.blueb.db.Database
import me.blueb.service.QueueService

private val queueService = QueueService()

fun Application.configureQueue() {
	SchemaHelpers.updateDatabaseSchema(
		Database.dataSource,
		listOf(
			queueService.inboxQueue,
			queueService.deliverQueue,
			queueService.systemQueue
		)
	)
}
