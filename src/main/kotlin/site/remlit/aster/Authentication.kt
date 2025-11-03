package site.remlit.aster

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.util.*
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import site.remlit.aster.common.model.type.RoleType
import site.remlit.aster.db.entity.UserEntity
import site.remlit.aster.model.ApiException
import site.remlit.aster.service.AuthService
import site.remlit.aster.service.RoleService
import site.remlit.aster.service.UserService

@ApiStatus.Internal
fun Application.configureAuthentication() {
	install(Authentication) {
		bearer("authOptional") {
			authenticate { credential ->
				val auth = AuthService.getByToken(credential.token)

				if (auth != null) {
					// todo: if older than 3 months, invalidate

					var authId = ""

					transaction {
						authId = auth.user.id.toString()
					}

					UserIdPrincipal(authId)

					val user = UserService.getById(authId)
					if (user != null && user.activated && !user.suspended) {
						attributes.put(AttributeKey<UserEntity>("authenticatedUser"), user)
					}
				}

				return@authenticate true
			}
		}

		bearer("authRequired") {
			authenticate { credential ->
				val auth = AuthService.getByToken(credential.token)
					?: throw ApiException(
						HttpStatusCode.Unauthorized,
						"Authentication required"
					)

				var authId = ""

				transaction {
					authId = auth.user.id.toString()
				}

				UserIdPrincipal(authId)

				val user = UserService.getById(authId)
					?: throw ApiException(
						HttpStatusCode.Unauthorized,
						"Authentication required"
					)

				attributes.put(AttributeKey<UserEntity>("authenticatedUser"), user)

				if (!user.activated || user.suspended)
					throw ApiException(HttpStatusCode.Forbidden, "Account inactive")

				return@authenticate true
			}
		}

		bearer("authRequiredMod") {
			authenticate { credential ->
				val auth = AuthService.getByToken(credential.token)
					?: throw ApiException(
						HttpStatusCode.Unauthorized,
						"Authentication required"
					)

				var authId = ""

				transaction {
					authId = auth.user.id.toString()
				}

				UserIdPrincipal(authId)

				val user = UserService.getById(authId)
					?: throw ApiException(
						HttpStatusCode.Unauthorized,
						"Authentication required"
					)

				attributes.put(AttributeKey<UserEntity>("authenticatedUser"), user)

				if (!user.activated || user.suspended)
					throw ApiException(HttpStatusCode.Forbidden, "Account inactive")

				val isMod = RoleService.userHasRoleOfType(user.id.toString(), RoleType.Mod)
				val isAdmin = RoleService.userHasRoleOfType(user.id.toString(), RoleType.Admin)

				if (!isMod && !isAdmin)
					throw ApiException(HttpStatusCode.Forbidden, "Mod role missing")

				return@authenticate true
			}
		}

		bearer("authRequiredAdmin") {
			authenticate { credential ->
				val auth = AuthService.getByToken(credential.token)
					?: throw ApiException(
						HttpStatusCode.Unauthorized,
						"Authentication required"
					)

				var authId = ""

				transaction {
					authId = auth.user.id.toString()
				}

				UserIdPrincipal(authId)

				val user = UserService.getById(authId)
					?: throw ApiException(HttpStatusCode.Unauthorized, "Authentication required")

				attributes.put(AttributeKey<UserEntity>("authenticatedUser"), user)

				if (!user.activated || user.suspended)
					throw ApiException(HttpStatusCode.Forbidden, "Account inactive")

				val isMod = RoleService.userHasRoleOfType(user.id.toString(), RoleType.Admin)

				if (!isMod)
					throw ApiException(HttpStatusCode.Forbidden, "Mod role missing")

				return@authenticate true
			}
		}
	}
}
