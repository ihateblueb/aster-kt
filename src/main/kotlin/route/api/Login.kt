package site.remlit.blueb.aster.route.api

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import site.remlit.blueb.aster.db.table.UserPrivateTable
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.model.ApiException
import site.remlit.blueb.aster.model.AuthResponse
import site.remlit.blueb.aster.model.User
import site.remlit.blueb.aster.service.AuthService
import site.remlit.blueb.aster.service.UserService

@Serializable
data class LoginBody(
	val username: String,
	val password: String
)

fun Route.login() {
	val authService = AuthService()
	val userService = UserService()

	post("/api/login") {
		val body = call.receive<LoginBody>()

		if (body.username.isBlank())
			throw ApiException(HttpStatusCode.BadRequest, "Username required")

		if (body.password.isBlank())
			throw ApiException(HttpStatusCode.BadRequest, "Password required")

		val user = userService.get(
			UserTable.username eq body.username
				and (UserTable.host eq null)
		)

		if (user == null)
			throw ApiException(HttpStatusCode.NotFound)

		val userPrivate = userService.getPrivate(
			UserPrivateTable.id eq user.id
		)

		if (userPrivate == null)
			throw ApiException(HttpStatusCode.BadRequest, "User's private table not found")

		val passwordValid = BCrypt.verifyer().verify(body.password.toCharArray(), userPrivate.password.toCharArray())

		if (!passwordValid.verified)
			throw ApiException(HttpStatusCode.BadRequest, "Incorrect password")

		val token = authService.registerToken(user)

		call.respond(AuthResponse(token, User.fromEntity(user)))
	}
}
