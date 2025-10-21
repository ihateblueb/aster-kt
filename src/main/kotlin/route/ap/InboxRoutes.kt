package site.remlit.blueb.aster.route.ap

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import site.remlit.blueb.aster.route.RouteRegistry
import site.remlit.blueb.aster.service.QueueService
import site.remlit.blueb.aster.service.ap.ApValidationService

object InboxRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			post("/inbox") {
				val body = call.receive<ByteArray>()
				val sender = ApValidationService.validate(call.request, body)
				QueueService.insertInboxJob(body, sender)
				call.respond(HttpStatusCode.OK)
			}

			post("/users/{id}/inbox") {
				val body = call.receive<ByteArray>()
				val sender = ApValidationService.validate(call.request, body)
				QueueService.insertInboxJob(body, sender)
				call.respond(HttpStatusCode.OK)
			}
		}
}
