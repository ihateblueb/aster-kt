package site.remlit.blueb

import io.ktor.server.application.*
import kolbasa.schema.SchemaHelpers
import site.remlit.blueb.db.Database
import site.remlit.blueb.service.QueueService

fun Application.configureQueue() {
	SchemaHelpers.updateDatabaseSchema(
		Database.dataSource,
		listOf(
			QueueService.inboxQueue,
			QueueService.deliverQueue,
			QueueService.systemQueue
		)
	)

	QueueService.initConsumers()
}
