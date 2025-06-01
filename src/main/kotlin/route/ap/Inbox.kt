package me.blueb.route.ap

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import me.blueb.service.ap.ApValidationService

private val apValidationService = ApValidationService()

fun Route.inbox() {
	post("/inbox") {
		apValidationService.validate(call)

		call.respond(HttpStatusCode.NotImplemented)
	}

	post("/user/{id}/inbox") {
		call.respond(HttpStatusCode.NotImplemented)
	}
}
