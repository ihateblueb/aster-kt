package me.blueb.route.ap

import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.util.getOrFail
import me.blueb.model.ap.ApActor
import me.blueb.service.UserService

fun Route.apUser() {
    val userService = UserService()

    get("/user/{id}") {
        val user = userService.getById(call.parameters.getOrFail("id"))

        if (user == null || user.host != null || !user.activated || user.suspended) {
            call.respond(HttpStatusCode.NotFound)
			return@get
        }

        call.respond(ApActor.fromEntity(user))
    }

	get("/user/{id}/followers") {
		call.respond(HttpStatusCode.NotImplemented)
	}

	get("/user/{id}/following") {
		call.respond(HttpStatusCode.NotImplemented)
	}
}
