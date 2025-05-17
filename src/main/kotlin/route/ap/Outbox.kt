package me.blueb.route.ap

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import me.blueb.model.Configuration

fun Route.outbox() {
	get("/outbox") {
		call.respond(HttpStatusCode.NotImplemented)
	}

	get("/user/{id}/outbox") {
		call.respond(HttpStatusCode.NotImplemented)
	}
}
