package site.remlit.blueb.aster

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.util.*
import site.remlit.blueb.aster.db.entity.UserEntity
import site.remlit.blueb.aster.db.suspendTransaction
import site.remlit.blueb.aster.model.ApiException
import site.remlit.blueb.aster.model.RoleType
import site.remlit.blueb.aster.service.AuthService
import site.remlit.blueb.aster.service.RoleService
import site.remlit.blueb.aster.service.UserService

fun Application.configureAuthentication() {
	install(Authentication) {
		bearer("authOptional") {
			authenticate { credential ->
				val auth = AuthService.getByToken(credential.token)

				if (auth != null) {
					// todo: if older than 3 months, invalidate

					var authId = ""

					suspendTransaction {
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

				if (auth == null)
					throw ApiException(HttpStatusCode.Unauthorized, "Authentication required")

				var authId = ""

				suspendTransaction {
					authId = auth.user.id.toString()
				}

				UserIdPrincipal(authId)

				val user = UserService.getById(authId)

				if (user == null)
					throw ApiException(HttpStatusCode.Unauthorized, "Authentication required")

				attributes.put(AttributeKey<UserEntity>("authenticatedUser"), user)

				if (!user.activated || user.suspended)
					throw ApiException(HttpStatusCode.Forbidden, "Account inactive")

				return@authenticate true
			}
		}

		bearer("authRequiredMod") {
			authenticate { credential ->
				val auth = AuthService.getByToken(credential.token)

				if (auth == null)
					throw ApiException(HttpStatusCode.Unauthorized, "Authentication required")

				var authId = ""

				suspendTransaction {
					authId = auth.user.id.toString()
				}

				UserIdPrincipal(authId)

				val user = UserService.getById(authId)

				if (user == null)
					throw ApiException(HttpStatusCode.Unauthorized, "Authentication required")

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

				if (auth == null)
					throw ApiException(HttpStatusCode.Unauthorized, "Authentication required")

				var authId = ""

				suspendTransaction {
					authId = auth.user.id.toString()
				}

				UserIdPrincipal(authId)

				val user = UserService.getById(authId)

				if (user == null)
					throw ApiException(HttpStatusCode.Unauthorized, "Authentication required")

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
