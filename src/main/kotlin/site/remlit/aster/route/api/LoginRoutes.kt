package site.remlit.aster.route.api

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import site.remlit.aster.common.model.User
import site.remlit.aster.common.model.response.AuthResponse
import site.remlit.aster.db.table.UserTable
import site.remlit.aster.model.ApiException
import site.remlit.aster.route.RouteRegistry
import site.remlit.aster.service.AuthService
import site.remlit.aster.service.UserService
import site.remlit.aster.util.model.fromEntity

object LoginRoutes {
	@Serializable
	data class LoginBody(
		val username: String,
		val password: String
	)

	fun register() =
		RouteRegistry.registerRoute {
			post("/api/login") {
				val body = call.receive<LoginBody>()

				if (body.username.isBlank())
					throw ApiException(HttpStatusCode.BadRequest, "Username required")

				if (body.password.isBlank())
					throw ApiException(HttpStatusCode.BadRequest, "Password required")

				val user = User.fromEntity(
					UserService.get(
						UserTable.username eq body.username
								and (UserTable.host eq null)
					) ?: throw ApiException(HttpStatusCode.NotFound)
				)

				val userPrivate = UserService.getPrivateById(user.id)
					?: throw ApiException(HttpStatusCode.BadRequest, "User's private table not found")

				val passwordValid =
					BCrypt.verifyer().verify(body.password.toCharArray(), userPrivate.password.toCharArray())

				if (!passwordValid.verified)
					throw ApiException(HttpStatusCode.BadRequest, "Incorrect password")

				val token = AuthService.registerToken(user.id)

				call.respond(AuthResponse(token, user))
			}
		}
}