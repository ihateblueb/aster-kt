package site.remlit.aster

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.annotations.ApiStatus
import site.remlit.aster.common.model.type.RoleType
import site.remlit.aster.model.ApiException
import site.remlit.aster.service.AuthService
import site.remlit.aster.service.RoleService
import site.remlit.aster.service.ap.ApValidationService
import site.remlit.aster.util.authenticatedUserKey
import site.remlit.aster.util.authenticatedUserRoleKey
import site.remlit.aster.util.authorizedFetchUserKey

private fun internalAuth(
	optional: Boolean,
	request: ApplicationRequest,
	attributes: Attributes,
	authorizedFetch: Boolean = false,
	requiredRole: RoleType? = null,
) {
	println("DBG-AUTH-O:$optional;AF:$authorizedFetch;RR:$requiredRole;")

	if (authorizedFetch) {
		val viewer = try {
			runBlocking { ApValidationService.validate(request as RoutingRequest) }
		} catch (e: Exception) {
			if (!optional) throw e
			else null
		}

		if (viewer != null && viewer.activated && !viewer.suspended) {
			attributes.put(
				authorizedFetchUserKey,
				viewer
			)
		}
	} else {
		val authHeader = request.headers["Authorization"]
		val authCookie = request.cookies["AsAuthorization"]
		val token = authHeader?.replace("Bearer ", "") ?: authCookie

		val unauthenticated = ApiException(
			HttpStatusCode.Unauthorized,
			"Authentication failed"
		)

		val authEntity = if (optional && token != null) {
			AuthService.getByToken(
				token
			)
		} else if (!optional) {
			AuthService.getByToken(
				token ?: throw unauthenticated
			) ?: throw unauthenticated
		} else {
			null
		}

		val user = authEntity?.user

		if (user != null && user.activated && !user.suspended) {
			attributes.put(
				authenticatedUserKey,
				user
			)

			val highestRole = RoleService.getUserHighestRole(user.id.toString()) ?: RoleType.Normal
			attributes.put(
				authenticatedUserRoleKey,
				highestRole
			)

			if (requiredRole != null) {
				when (requiredRole) {
					RoleType.Mod -> if (highestRole != RoleType.Admin && highestRole != RoleType.Mod)
						throw unauthenticated

					RoleType.Admin -> if (highestRole != RoleType.Admin)
						throw unauthenticated

					else -> {}
				}
			}
		}
	}
}

@ApiStatus.Internal
fun Application.configureAuthentication() {
	install(Authentication) {
		bearer("authOptional") {
			authenticate {
				internalAuth(
					true,
					request,
					attributes
				)
			}
		}

		bearer("authRequired") {
			authenticate {
				internalAuth(
					false,
					request,
					attributes
				)
			}
		}

		bearer("authRequiredMod") {
			authenticate {
				internalAuth(
					false,
					request,
					attributes,
					requiredRole = RoleType.Mod
				)
			}
		}

		bearer("authRequiredAdmin") {
			authenticate {
				internalAuth(
					false,
					request,
					attributes,
					requiredRole = RoleType.Admin
				)
			}
		}

		bearer("authorizedFetchOptional") {
			authenticate {
				internalAuth(
					true,
					request,
					attributes,
					true
				)
			}
		}

		bearer("authorizedFetchRequired") {
			authenticate {
				internalAuth(
					false,
					request,
					attributes,
					true
				)
			}
		}
	}
}
