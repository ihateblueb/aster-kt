package site.remlit.blueb

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.response.*
import io.ktor.util.*
import site.remlit.blueb.db.entity.UserEntity
import site.remlit.blueb.db.suspendTransaction
import site.remlit.blueb.model.ApiError
import site.remlit.blueb.model.RoleType
import site.remlit.blueb.service.AuthService
import site.remlit.blueb.service.RoleService
import site.remlit.blueb.service.UserService

private val authService = AuthService()
private val userService = UserService()
private val roleService = RoleService()

val authenticatedUserKey = AttributeKey<UserEntity>("authenticatedUser")

fun Application.configureAuthentication() {
	install(Authentication) {
		bearer("authOptional") {
			authenticate { credential ->
				val auth = authService.getByToken(credential.token)

				if (auth != null) {
					// todo: if older than 3 months, invalidate

					var authId = ""

					suspendTransaction {
						authId = auth.user.id.toString()
					}

					UserIdPrincipal(authId)

					val user = userService.getById(authId)
					if (user != null && user.activated && !user.suspended) {
						attributes.put(AttributeKey<UserEntity>("authenticatedUser"), user)
					}
				} else {
					null
				}
			}
		}

		bearer("authRequired") {
			authenticate { credential ->
				val auth = authService.getByToken(credential.token)

				if (auth != null) {
					var authId = ""

					suspendTransaction {
						authId = auth.user.id.toString()
					}

					UserIdPrincipal(authId)

					val user = userService.getById(authId)
					if (user != null) {
						attributes.put(AttributeKey<UserEntity>("authenticatedUser"), user)

						if (!user.activated || user.suspended) {
							respond(
								HttpStatusCode.Forbidden,
								ApiError(
									message = "Account inactive",
									callId = callId
								)
							)
							return@authenticate false
						} else {
							return@authenticate true
						}
					} else {
						respond(
							HttpStatusCode.Unauthorized,
							ApiError(
								message = "Authentication required",
								callId = callId
							)
						)
						return@authenticate false
					}
				} else {
					respond(
						HttpStatusCode.Unauthorized,
						ApiError(
							message = "Authentication required",
							callId = callId
						)
					)
					return@authenticate false
				}
			}
		}

		bearer("authRequiredMod") {
			authenticate { credential ->
				val auth = authService.getByToken(credential.token)

				if (auth != null) {
					var authId = ""

					suspendTransaction {
						authId = auth.user.id.toString()
					}

					UserIdPrincipal(authId)

					val user = userService.getById(authId)
					if (user != null) {
						attributes.put(AttributeKey<UserEntity>("authenticatedUser"), user)

						if (!user.activated || user.suspended) {
							respond(
								HttpStatusCode.Forbidden,
								ApiError(
									message = "Account inactive",
									callId = callId
								)
							)
							return@authenticate false
						} else {
							val isMod = roleService.userHasRoleOfType(user.id.toString(), RoleType.Mod)

							if (!isMod) {
								respond(
									HttpStatusCode.Forbidden,
									ApiError(
										message = "Account does not have a mod role",
										callId = callId
									)
								)
								return@authenticate false
							}
						}
					} else {
						respond(
							HttpStatusCode.Unauthorized,
							ApiError(
								message = "Authentication required",
								callId = callId
							)
						)
						return@authenticate false
					}
				} else {
					respond(
						HttpStatusCode.Unauthorized,
						ApiError(
							message = "Authentication required",
							callId = callId
						)
					)
					return@authenticate false
				}
			}
		}

		bearer("authRequiredAdmin") {
			authenticate { credential ->
				val auth = authService.getByToken(credential.token)

				if (auth != null) {
					var authId = ""

					suspendTransaction {
						authId = auth.user.id.toString()
					}

					UserIdPrincipal(authId)

					val user = userService.getById(authId)
					if (user != null) {
						attributes.put(AttributeKey<UserEntity>("authenticatedUser"), user)

						if (!user.activated || user.suspended) {
							respond(
								HttpStatusCode.Forbidden,
								ApiError(
									message = "Account inactive",
									callId = callId
								)
							)
							return@authenticate false
						} else {
							val isAdmin = roleService.userHasRoleOfType(user.id.toString(), RoleType.Admin)

							if (!isAdmin) {
								respond(
									HttpStatusCode.Forbidden,
									ApiError(
										message = "Account does not have an admin role",
										callId = callId
									)
								)
								return@authenticate false
							}
						}
					} else {
						respond(
							HttpStatusCode.Unauthorized,
							ApiError(
								message = "Authentication required",
								callId = callId
							)
						)
						return@authenticate false
					}
				} else {
					respond(
						HttpStatusCode.Unauthorized,
						ApiError(
							message = "Authentication required",
							callId = callId
						)
					)
					return@authenticate false
				}
			}
		}
	}
}
