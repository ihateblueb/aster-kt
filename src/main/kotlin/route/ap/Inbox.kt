package site.remlit.blueb.route.ap

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import site.remlit.blueb.service.ap.ApUtilityService
import site.remlit.blueb.service.ap.ApValidationService

private val apValidationService = ApValidationService()
private val apUtilityService = ApUtilityService()

fun Route.inbox() {
	post("/inbox") {
		val body = call.receive<ByteArray>()

		apValidationService.validate(call.request, body)

		apUtilityService.byteArrayToObject(body)

		call.respond(HttpStatusCode.NotImplemented)
	}

	post("/user/{id}/inbox") {
		call.respond(HttpStatusCode.NotImplemented)
	}
}
