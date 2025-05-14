package me.blueb.route.api

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.callid.callId
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.Serializable
import me.blueb.db.table.UserPrivateTable
import me.blueb.db.table.UserTable
import me.blueb.model.ApiError
import me.blueb.service.AuthService
import me.blueb.service.UserService
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and

@Serializable
data class LoginBody(
	val username: String,
	val password: String
)

@Serializable
data class LoginResponse(
	val token: String
)

fun Route.login() {
	val authService = AuthService()
	val userService = UserService()

    post("/api/login") {
		val body = call.receive<LoginBody>()

		if (body.username.isBlank()) {
			call.respond(
				status = HttpStatusCode.BadRequest,
				message = ApiError(
					message = "Username required",
					requestId = call.callId
				)
			)
			return@post
		}

		if (body.password.isBlank()) {
			call.respond(
				status = HttpStatusCode.BadRequest,
				message = ApiError(
					message = "Password required",
					requestId = call.callId
				)
			)
			return@post
		}

		val user = userService.get(
			UserTable.username eq body.username
				and(UserTable.host eq null)
		)

		if (user == null) {
			call.respond(
				status = HttpStatusCode.NotFound,
				message = ApiError(
					message = "User not found",
					requestId = call.callId
				)
			)
			return@post
		}

		val userPrivate = userService.getPrivate(
			UserPrivateTable.id eq user.id
		)

		if (userPrivate == null) {
			call.respond(
				status = HttpStatusCode.NotFound,
				message = ApiError(
					message = "User's private table not found",
					requestId = call.callId
				)
			)
			return@post
		}

		val passwordValid = BCrypt.verifyer().verify(body.password.toCharArray(), userPrivate.password.toCharArray())

		if (!passwordValid.verified) {
			call.respond(
				status = HttpStatusCode.BadRequest,
				message = ApiError(
					message = "Incorrect password",
					requestId = call.callId
				)
			)
			return@post
		}

		val token = authService.registerToken(user.id.toString())

		call.respond(LoginResponse(token))
    }
}
