package site.remlit.blueb

import io.ktor.server.application.*
import kolbasa.schema.SchemaHelpers
import site.remlit.blueb.db.Database
import site.remlit.blueb.queue.Queues

fun Application.configureQueue() {
	SchemaHelpers.updateDatabaseSchema(
		Database.dataSource,
		listOf(
			Queues.inboxQueue,
			Queues.deliverQueue,
			Queues.systemQueue
		)
	)

	Queues.initConsumers()
}
