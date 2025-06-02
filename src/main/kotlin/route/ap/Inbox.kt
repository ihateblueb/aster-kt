package site.remlit.blueb.route.ap

import io.ktor.http.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import site.remlit.blueb.model.ApiError
import site.remlit.blueb.model.ap.ApValidationException
import site.remlit.blueb.model.ap.ApValidationExceptionType
import site.remlit.blueb.service.QueueService
import site.remlit.blueb.service.ap.ApUtilityService
import site.remlit.blueb.service.ap.ApValidationService

private val apValidationService = ApValidationService()
private val apUtilityService = ApUtilityService()

fun Route.inbox() {
	post("/inbox") {
		val body = call.receive<ByteArray>()

		try {
			apValidationService.validate(call.request, body)
		} catch (e: ApValidationException) {
			call.respond(
				if (e.type == ApValidationExceptionType.Unauthorized) HttpStatusCode.Unauthorized else HttpStatusCode.Forbidden,
				ApiError(
					e.message,
					call.callId
				)
			)
		}

		QueueService.producer.send(QueueService.inboxQueue, String(body))

		call.respond(HttpStatusCode.OK)
	}

	post("/user/{id}/inbox") {
		call.respond(HttpStatusCode.NotImplemented)
	}
}
