package site.remlit.blueb.aster

import io.ktor.server.application.*
import site.remlit.blueb.aster.service.QueueService

fun Application.configureQueue() {
	QueueService.init()
}
