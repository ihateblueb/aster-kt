package site.remlit.blueb.route.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import site.remlit.blueb.model.Meta

fun Route.meta() {
	get("/api/meta") {
		call.respond(HttpStatusCode.OK, Meta.get())
	}
}
