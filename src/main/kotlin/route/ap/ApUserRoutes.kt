package site.remlit.blueb.aster.route.ap

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import site.remlit.blueb.aster.model.ApiException
import site.remlit.blueb.aster.model.ap.ApActor
import site.remlit.blueb.aster.route.RouteRegistry
import site.remlit.blueb.aster.service.UserService

object ApUserRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			get("/users/{id}") {
				val user = UserService.getById(call.parameters.getOrFail("id"))

				if (user == null || user.host != null || !user.activated || user.suspended)
					throw ApiException(HttpStatusCode.NotFound)

				call.respond(ApActor.fromEntity(user))
			}

			get("/users/{id}/followers") {
				throw ApiException(HttpStatusCode.NotImplemented)
			}

			get("/users/{id}/following") {
				throw ApiException(HttpStatusCode.NotImplemented)
			}
		}
}
