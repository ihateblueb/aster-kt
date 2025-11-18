package site.remlit.aster.util.auth

import io.ktor.server.routing.*
import org.jetbrains.annotations.ApiStatus

/**
 * Ktor RouteSelector for authentication
 * */
@ApiStatus.Internal
class AuthRouteSelector : RouteSelector() {
	override suspend fun evaluate(
		context: RoutingResolveContext,
		segmentIndex: Int
	): RouteSelectorEvaluation =
		RouteSelectorEvaluation.Transparent
}