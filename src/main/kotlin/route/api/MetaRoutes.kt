package site.remlit.blueb.aster.route.api

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import site.remlit.blueb.aster.model.Meta
import site.remlit.blueb.aster.route.RouteRegistry

object MetaRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			get("/api/meta") {
				call.respond(HttpStatusCode.OK, Meta.get())
			}
		}
}
