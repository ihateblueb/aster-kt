package site.remlit.blueb.aster

import io.ktor.server.application.*
import kolbasa.schema.SchemaHelpers
import site.remlit.blueb.aster.db.Database
import site.remlit.blueb.aster.queue.Queues

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
