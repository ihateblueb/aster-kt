package site.remlit.blueb.route.ap

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.outbox() {
	get("/outbox") {
		call.respond(HttpStatusCode.NotImplemented)
	}

	get("/user/{id}/outbox") {
		call.respond(HttpStatusCode.NotImplemented)
	}
}
