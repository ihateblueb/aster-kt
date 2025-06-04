package site.remlit.blueb.route.ap

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import site.remlit.blueb.model.ApiException
import site.remlit.blueb.model.ap.ApActor
import site.remlit.blueb.service.UserService

fun Route.apUser() {
	val userService = UserService()

	get("/users/{id}") {
		val user = userService.getById(call.parameters.getOrFail("id"))

		if (user == null || user.host != null || !user.activated || user.suspended)
			throw ApiException(HttpStatusCode.NotFound)

		call.respond(ApActor.fromEntity(user))
	}

	get("/users/{id}/followers") {
		throw ApiException(HttpStatusCode.NotImplemented)
	}

	get("/users/{id}/following") {
		throw ApiException(HttpStatusCode.NotImplemented)
	}
}
