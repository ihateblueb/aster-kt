package site.remlit.blueb.aster.route.api

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.model.ApiException
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.User
import site.remlit.blueb.aster.route.RouteRegistry
import site.remlit.blueb.aster.service.RelationshipService
import site.remlit.blueb.aster.service.UserService
import site.remlit.blueb.aster.util.authenticatedUserKey

object UserRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			authenticate("authOptional") {
				get("/api/lookup/{handle}") {
					val handle = call.parameters.getOrFail("handle").removePrefix("@")
					val splitHandle = handle.split("@")

					val host = if (splitHandle.size > 1) splitHandle[1].ifEmpty { null } else null

					val user = UserService.get(
						UserTable.username eq splitHandle[0]
								and (UserTable.host eq host)
					)

					if (user == null || !user.activated || user.suspended ||
						(Configuration.hideRemoteContent && user.host != null && call.attributes.getOrNull(
							authenticatedUserKey
						) == null)
					)
						throw ApiException(HttpStatusCode.NotFound)

					call.respond(User.fromEntity(user))
				}
			}

			get("/api/user/{id}") {
				val user = UserService.getById(call.parameters.getOrFail("id"))

				if (user == null || !user.activated || user.suspended)
					throw ApiException(HttpStatusCode.NotFound)

				call.respond(
					User.fromEntity(user)
				)
			}

			authenticate("authRequired") {
				patch("/api/user/{id}") {
					val user = UserService.getById(call.parameters.getOrFail("id"))

					if (user == null || !user.activated || user.suspended)
						throw ApiException(HttpStatusCode.NotFound)

					throw ApiException(HttpStatusCode.NotImplemented)
				}

				post("/api/user/{id}/mute") {
					val user = UserService.getById(call.parameters.getOrFail("id"))

					if (user == null || !user.activated || user.suspended)
						throw ApiException(HttpStatusCode.NotFound)

					throw ApiException(HttpStatusCode.NotImplemented)
				}

				post("/api/user/{id}/block") {
					val user = UserService.getById(call.parameters.getOrFail("id"))

					if (user == null || !user.activated || user.suspended)
						throw ApiException(HttpStatusCode.NotFound)

					throw ApiException(HttpStatusCode.NotImplemented)
				}

				post("/api/user/{id}/refetch") {
					val user = UserService.getById(call.parameters.getOrFail("id"))

					if (user == null || !user.activated || user.suspended)
						throw ApiException(HttpStatusCode.NotFound)

					if (user.host == null)
						throw ApiException(HttpStatusCode.BadRequest, "Local users can't be refetched")

					throw ApiException(HttpStatusCode.NotImplemented)
				}

				get("/api/user/{id}/relationship") {
					val user = UserService.getById(call.parameters.getOrFail("id"))

					if (user == null || !user.activated || user.suspended)
						throw ApiException(HttpStatusCode.NotFound)

					val requestingUser = call.attributes[authenticatedUserKey]

					call.respond(
						RelationshipService.mapPair(
							RelationshipService.getPair(requestingUser.id.toString(), user.id.toString())
						)
					)
				}
			}
		}
}
