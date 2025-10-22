package site.remlit.blueb.aster

import io.ktor.server.application.*
import site.remlit.blueb.aster.registry.InboxHandlerRegistry
import site.remlit.blueb.aster.service.QueueService

fun Application.configureQueue() {
	InboxHandlerRegistry.registerDefaults()
	QueueService.init()
}
