package site.remlit.blueb.aster.route.ap

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import site.remlit.blueb.aster.service.ap.ApValidationService

fun Route.inbox() {
	post("/inbox") {
		val body = call.receive<ByteArray>()
		ApValidationService.validate(call.request, body)
		println("SUCCESS ON INBOX!")
		// push
		call.respond(HttpStatusCode.OK)
	}

	post("/users/{id}/inbox") {
		val body = call.receive<ByteArray>()
		ApValidationService.validate(call.request, body)
		println("SUCCESS ON INBOX!")
		// push
		call.respond(HttpStatusCode.OK)
	}
}
