package site.remlit.blueb.route.ap

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import site.remlit.blueb.model.ApiException
import site.remlit.blueb.queue.message.InboxQueueMessage
import site.remlit.blueb.queue.Queues
import site.remlit.blueb.service.ap.ApUtilityService
import site.remlit.blueb.service.ap.ApValidationService

fun Route.inbox() {
	val apValidationService = ApValidationService()
	val apUtilityService = ApUtilityService()

	post("/inbox") {
		val body = call.receive<ByteArray>()

		apValidationService.validate(call.request, body)

		Queues.producer.send(Queues.inboxQueue, InboxQueueMessage("", String(body)).toString())

		call.respond(HttpStatusCode.OK)
	}

	post("/user/{id}/inbox") {
		throw ApiException(HttpStatusCode.NotImplemented)
	}
}
