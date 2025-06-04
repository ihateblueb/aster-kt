package site.remlit.blueb.route.ap

import io.ktor.http.*
import io.ktor.server.routing.*
import site.remlit.blueb.model.ApiException

fun Route.outbox() {
	get("/outbox") {
		throw ApiException(HttpStatusCode.NotImplemented)
	}

	get("/user/{id}/outbox") {
		throw ApiException(HttpStatusCode.NotImplemented)
	}
}
