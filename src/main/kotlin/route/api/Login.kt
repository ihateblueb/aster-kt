package site.remlit.blueb.route.api

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.http.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import site.remlit.blueb.db.table.UserPrivateTable
import site.remlit.blueb.db.table.UserTable
import site.remlit.blueb.model.ApiError
import site.remlit.blueb.model.User
import site.remlit.blueb.service.AuthService
import site.remlit.blueb.service.UserService

@Serializable
data class LoginBody(
	val username: String,
	val password: String
)

@Serializable
data class LoginResponse(
	val token: String,
	val user: User
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
					callId = call.callId
				)
			)
			return@post
		}

		if (body.password.isBlank()) {
			call.respond(
				status = HttpStatusCode.BadRequest,
				message = ApiError(
					message = "Password required",
					callId = call.callId
				)
			)
			return@post
		}

		val user = userService.get(
			UserTable.username eq body.username
				and (UserTable.host eq null)
		)

		if (user == null) {
			call.respond(
				status = HttpStatusCode.NotFound,
				message = ApiError(
					message = "User not found",
					callId = call.callId
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
					callId = call.callId
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
					callId = call.callId
				)
			)
			return@post
		}

		val token = authService.registerToken(user)

		call.respond(LoginResponse(token, User.fromEntity(user)))
	}
}
