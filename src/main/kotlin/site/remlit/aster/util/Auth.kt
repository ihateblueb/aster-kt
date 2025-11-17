package site.remlit.aster.util

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.util.*
import site.remlit.aster.common.model.type.RoleType
import site.remlit.aster.db.entity.UserEntity

val authenticatedUserKey = AttributeKey<UserEntity>("authenticatedUser")
val authenticatedUserRoleKey = AttributeKey<RoleType>("authenticatedUserRole")
val authorizedFetchUserKey = AttributeKey<UserEntity>("authorizedFetchUser")

/**
 * Used for authentication of routes.
 * Simplifies calls to Ktor's Authentication plugin.
 *
 * @param required If authentication is required to access routes
 * @param role Role type required to access routes
 * @param route Route block
 *
 * @return Route block wrapped in authentication
 * */
fun Route.authentication(
	required: Boolean = false,
	role: RoleType? = null,
	route: Route.() -> Unit
): Route {
	if (required) {
		if (role != null) {
			return when (role) {
				RoleType.Admin ->
					authenticate("authRequiredAdmin") {
						route()
					}

				RoleType.Mod ->
					authenticate("authRequiredAdmin") {
						route()
					}

				else ->
					throw IllegalArgumentException("Cannot require this authentication type")
			}
		} else {
			return authenticate("authRequired") {
				route()
			}
		}
	} else {
		return authenticate("authOptional") {
			route()
		}
	}
}

fun authorizedFetch(route: Route.() -> Unit) {

}