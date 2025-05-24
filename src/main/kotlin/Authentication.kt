package me.blueb

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.bearer
import io.ktor.server.plugins.callid.callId
import io.ktor.server.response.respond
import io.ktor.util.AttributeKey
import me.blueb.db.entity.UserEntity
import me.blueb.db.suspendTransaction
import me.blueb.model.ApiError
import me.blueb.model.RoleType
import me.blueb.service.AuthService
import me.blueb.service.RoleService
import me.blueb.service.UserService

private val authService = AuthService()
private val userService = UserService()
private val roleService = RoleService()

val authenticatedUserKey = AttributeKey<UserEntity>("authenticatedUser")

fun Application.configureAuthentication() {
	install(Authentication) {
		bearer("authOptional") {
			authenticate { credential ->
				val auth = authService.getByToken(credential.token)

				if (auth != null ) {
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
									requestId = callId
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
								requestId = callId
							)
						)
						return@authenticate false
					}
				} else {
					respond(
						HttpStatusCode.Unauthorized,
						ApiError(
							message = "Authentication required",
							requestId = callId
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
									requestId = callId
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
										requestId = callId
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
								requestId = callId
							)
						)
						return@authenticate false
					}
				} else {
					respond(
						HttpStatusCode.Unauthorized,
						ApiError(
							message = "Authentication required",
							requestId = callId
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
									requestId = callId
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
										requestId = callId
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
								requestId = callId
							)
						)
						return@authenticate false
					}
				} else {
					respond(
						HttpStatusCode.Unauthorized,
						ApiError(
							message = "Authentication required",
							requestId = callId
						)
					)
					return@authenticate false
				}
			}
		}
	}
}
