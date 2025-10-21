package site.remlit.blueb.aster.route.api

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import site.remlit.blueb.aster.common.model.User
import site.remlit.blueb.aster.common.model.generated.PartialUser
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.event.user.UserEditEvent
import site.remlit.blueb.aster.model.ApiException
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.route.RouteRegistry
import site.remlit.blueb.aster.service.NotificationService
import site.remlit.blueb.aster.service.RelationshipService
import site.remlit.blueb.aster.service.RoleService
import site.remlit.blueb.aster.service.TimeService
import site.remlit.blueb.aster.service.UserService
import site.remlit.blueb.aster.util.authenticatedUserKey
import site.remlit.blueb.aster.util.model.fromEntity
import site.remlit.blueb.aster.util.sanitizeOrNull

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
				post("/api/user/{id}") {
					val authenticatedUser = call.attributes[authenticatedUserKey]
					val user = UserService.getById(call.parameters.getOrFail("id"))

					if (user == null || !user.activated || user.suspended)
						throw ApiException(HttpStatusCode.NotFound)

					if (user.id != authenticatedUser.id && !RoleService.isModOrAdmin(authenticatedUser.id.toString()))
						throw ApiException(HttpStatusCode.BadRequest, "You don't have permission to edit this users")

					val originalHashCode = user.hashCode()
					val partial = call.receive<PartialUser>()

					// todo: these need to be in transaction
					user.displayName = sanitizeOrNull { partial.displayName }
					user.bio = sanitizeOrNull { partial.bio }
					user.location = sanitizeOrNull { partial.location }
					user.birthday = sanitizeOrNull { partial.birthday }

					user.avatar = sanitizeOrNull { partial.avatar }
					user.avatarAlt = sanitizeOrNull { partial.avatarAlt }
					user.banner = sanitizeOrNull { partial.banner }
					user.bannerAlt = sanitizeOrNull { partial.bannerAlt }

					user.locked = partial.locked ?: false
					user.automated = partial.automated ?: false
					user.discoverable = partial.discoverable ?: false
					user.indexable = partial.indexable ?: false
					user.sensitive = partial.sensitive ?: false

					user.isCat = partial.isCat ?: false
					user.speakAsCat = partial.speakAsCat ?: false

					if (user.hashCode() == originalHashCode)
						return@post call.respond(HttpStatusCode.OK, user)

					user.updatedAt = TimeService.now()

					// todo: overkill?
					user.flush()
					user.refresh()

					val model = User.fromEntity(user)
					UserEditEvent(model).call()

					return@post call.respond(HttpStatusCode.OK, model)
				}

				post("/api/user/{id}/bite") {
					val authenticatedUser = call.attributes[authenticatedUserKey]
					val user = UserService.getById(call.parameters.getOrFail("id"))

					if (user == null || !user.activated || user.suspended)
						throw ApiException(HttpStatusCode.NotFound)

					if (user.id == authenticatedUser.id)
						throw ApiException(HttpStatusCode.BadRequest, "You can't bite yourself")

					if (RelationshipService.eitherBlocking(user.id.toString(), authenticatedUser.id.toString()))
						throw ApiException(HttpStatusCode.Forbidden)

					NotificationService.bite(user, authenticatedUser)

					throw ApiException(HttpStatusCode.OK)
				}

				post("/api/user/{id}/follow") {
					val user = UserService.getById(call.parameters.getOrFail("id"))

					if (user == null || !user.activated || user.suspended)
						throw ApiException(HttpStatusCode.NotFound)

					throw ApiException(HttpStatusCode.NotImplemented)
				}

				post("/api/user/{id}/report") {
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
