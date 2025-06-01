package me.blueb.route.ap

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import me.blueb.service.ap.ApUtilityService
import me.blueb.service.ap.ApValidationService

private val apValidationService = ApValidationService()
private val apUtilityService = ApUtilityService()

fun Route.inbox() {
	post("/inbox") {
		val body = call.receive<ByteArray>()

		apValidationService.validate(call.request, body)

		val bodyObject = apUtilityService.byteArrayToObject(body)

		call.respond(HttpStatusCode.NotImplemented)
	}

	post("/user/{id}/inbox") {
		call.respond(HttpStatusCode.NotImplemented)
	}
}
