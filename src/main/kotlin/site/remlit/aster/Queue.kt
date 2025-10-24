package site.remlit.aster

import io.ktor.server.application.*
import site.remlit.aster.registry.InboxHandlerRegistry
import site.remlit.aster.service.QueueService

fun Application.configureQueue() {
	InboxHandlerRegistry.registerDefaults()
	QueueService.init()
}
