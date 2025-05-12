package me.blueb.route.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.callid.callId
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingCall
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.util.getOrFail
import me.blueb.model.ApiError
import me.blueb.model.User
import me.blueb.service.UserService

fun Route.user() {
    val userService = UserService()

    get("/api/user/{id}") {
        val user = userService.getById(call.parameters.getOrFail("id"))

        if (user == null || !user.activated || user.suspended) {
			call.respond(HttpStatusCode.NotFound)
			return@get
        }

        call.respond(
            User.fromEntity(user)
        )
    }

	patch("/api/user/{id}") {
		val user = userService.getById(call.parameters.getOrFail("id"))

		if (user == null || !user.activated || user.suspended) {
			call.respond(HttpStatusCode.NotFound)
			return@patch
		}

		call.respond(HttpStatusCode.NotImplemented)
	}

	post("/api/user/{id}/mute") {
		val user = userService.getById(call.parameters.getOrFail("id"))

		if (user == null || !user.activated || user.suspended) {
			call.respond(HttpStatusCode.NotFound)
			return@post
		}

		call.respond(HttpStatusCode.NotImplemented)
	}

	post("/api/user/{id}/block") {
		val user = userService.getById(call.parameters.getOrFail("id"))

		if (user == null || !user.activated || user.suspended) {
			call.respond(HttpStatusCode.NotFound)
			return@post
		}

		call.respond(HttpStatusCode.NotImplemented)
	}

	post("/api/user/{id}/refetch") {
		val user = userService.getById(call.parameters.getOrFail("id"))

		if (user == null || !user.activated || user.suspended) {
			call.respond(HttpStatusCode.NotFound)
			return@post
		}

		call.respond(HttpStatusCode.NotImplemented)
	}
}
