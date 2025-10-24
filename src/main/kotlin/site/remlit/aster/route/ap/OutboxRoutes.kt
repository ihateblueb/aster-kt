package site.remlit.aster.route.ap

import io.ktor.http.*
import io.ktor.server.routing.*
import site.remlit.aster.model.ApiException
import site.remlit.aster.route.RouteRegistry

object OutboxRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			get("/outbox") {
				throw ApiException(HttpStatusCode.NotImplemented)
			}

			get("/user/{id}/outbox") {
				throw ApiException(HttpStatusCode.NotImplemented)
			}
		}
}
