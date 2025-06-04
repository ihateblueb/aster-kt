package site.remlit.blueb.route.api

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import site.remlit.blueb.db.table.UserTable
import site.remlit.blueb.model.User
import site.remlit.blueb.service.UserService

fun Route.user() {
	val userService = UserService()

	get("/api/lookup/{handle}") {
		val handle = call.parameters.getOrFail("handle").removePrefix("@")
		val splitHandle = handle.split("@")

		val host = if (splitHandle.size > 1) splitHandle[1].ifEmpty { null } else null

		val user = userService.get(
			UserTable.username eq splitHandle[0]
				and (UserTable.host eq host)
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

		throw ApiException(HttpStatusCode.NotImplemented)
	}

	post("/api/user/{id}/mute") {
		val user = userService.getById(call.parameters.getOrFail("id"))

		if (user == null || !user.activated || user.suspended) {
			call.respond(HttpStatusCode.NotFound)
			return@post
		}

		throw ApiException(HttpStatusCode.NotImplemented)
	}

	post("/api/user/{id}/block") {
		val user = userService.getById(call.parameters.getOrFail("id"))

		if (user == null || !user.activated || user.suspended) {
			call.respond(HttpStatusCode.NotFound)
			return@post
		}

		throw ApiException(HttpStatusCode.NotImplemented)
	}

	post("/api/user/{id}/refetch") {
		val user = userService.getById(call.parameters.getOrFail("id"))

		if (user == null || !user.activated || user.suspended) {
			call.respond(HttpStatusCode.NotFound)
			return@post
		}

		throw ApiException(HttpStatusCode.NotImplemented)
	}
}
