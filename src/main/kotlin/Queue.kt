package me.blueb

import io.ktor.server.application.*
import kolbasa.queue.PredefinedDataTypes
import kolbasa.queue.Queue
import kolbasa.schema.SchemaHelpers
import me.blueb.db.Database

fun Application.configureQueue() {
	val inboxQueue = Queue.of("inbox", PredefinedDataTypes.String)
	val deliverQueue = Queue.of("deliver", PredefinedDataTypes.String)

	SchemaHelpers.updateDatabaseSchema(Database.dataSource, listOf(inboxQueue, deliverQueue))
}
