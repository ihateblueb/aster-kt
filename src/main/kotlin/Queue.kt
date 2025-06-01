package site.remlit.blueb

import io.ktor.server.application.*
import kolbasa.schema.SchemaHelpers
import site.remlit.blueb.db.Database
import site.remlit.blueb.service.QueueService

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

	queueService.initConsumers()
}
