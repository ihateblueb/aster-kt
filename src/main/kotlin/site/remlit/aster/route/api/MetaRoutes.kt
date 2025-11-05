package site.remlit.aster.route.api

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import site.remlit.aster.common.model.Meta
import site.remlit.aster.registry.RouteRegistry
import site.remlit.aster.util.model.getMeta

object MetaRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			get("/api/meta") {
				call.respond(HttpStatusCode.OK, Meta.getMeta())
			}
		}
}
