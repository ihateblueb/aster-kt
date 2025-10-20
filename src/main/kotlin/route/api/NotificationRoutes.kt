package site.remlit.blueb.aster.route.api

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import site.remlit.blueb.aster.route.RouteRegistry

object NotificationRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			authenticate("authRequired") {
				get("/api/notifications") {
					call.respond(HttpStatusCode.NotImplemented)
				}

				delete("/api/notification/{id}") {
					call.respond(HttpStatusCode.NotImplemented)
				}
			}
		}
}