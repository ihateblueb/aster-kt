package site.remlit.aster.util

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.util.*
import site.remlit.aster.common.model.type.RoleType
import site.remlit.aster.db.entity.UserEntity
import site.remlit.aster.model.ApiException
import site.remlit.aster.service.AuthService
import site.remlit.aster.service.RoleService

val authenticatedUserKey = AttributeKey<UserEntity>("authenticatedUser")
val authenticatedUserRoleKey = AttributeKey<RoleType>("authenticatedUserRole")

val auth = createRouteScopedPlugin("auth") {
	onCall { call ->
		val authHeader = call.request.headers["Authorization"]
		val authCookie = call.request.cookies["AsAuthorization"]

		val unauthenticated = ApiException(
			HttpStatusCode.Unauthorized,
			"Authentication failed"
		)

		val authEntity = AuthService.getByToken(
			authHeader ?: authCookie ?: throw unauthenticated
		) ?: throw unauthenticated

		val user = authEntity.user

		call.attributes.put(
			authenticatedUserKey,
			user
		)

		call.attributes.put(
			authenticatedUserRoleKey,
			RoleService.getUserHighestRole(user.id.toString()) ?: RoleType.Normal
		)
	}
}

val authOptional = createRouteScopedPlugin("authOptional") {
	onCall { call ->
		val authHeader = call.request.headers["Authorization"]
		val authCookie = call.request.cookies["AsAuthorization"]
		val token = authHeader ?: authCookie

		if (token == null) return@onCall

		val authEntity = AuthService.getByToken(
			token
		)

		if (authEntity == null) return@onCall

		val user = authEntity.user

		call.attributes.put(
			authenticatedUserKey,
			user
		)

		call.attributes.put(
			authenticatedUserRoleKey,
			RoleService.getUserHighestRole(user.id.toString()) ?: RoleType.Normal
		)
	}
}