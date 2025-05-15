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
import me.blueb.db.table.UserTable
import me.blueb.model.User
import me.blueb.service.UserService
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and

fun Route.user() {
    val userService = UserService()

	get("/api/lookup/{handle}") {
		val handle = call.parameters.getOrFail("handle").removePrefix("@")
		val splitHandle = handle.split("@")

		val host = if (splitHandle.size > 1) splitHandle[1].ifEmpty { null } else null

		val user = userService.get(
			UserTable.username eq splitHandle[0]
				and(UserTable.host eq host)
		)

		if (user == null || !user.activated || user.suspended) {
			call.respond(HttpStatusCode.NotFound)
			return@get
		}

		call.respond(User.fromEntity(user))
	}

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
