package me.blueb.route.ap

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import me.blueb.model.Configuration

fun Route.inbox() {
	val configuration = Configuration()

	post("/inbox") {
		call.respond(HttpStatusCode.NotImplemented)
	}
}
