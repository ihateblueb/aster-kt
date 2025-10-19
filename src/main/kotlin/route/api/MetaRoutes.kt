package site.remlit.blueb.aster.route.api

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import site.remlit.blueb.aster.common.model.Meta
import site.remlit.blueb.aster.route.RouteRegistry
import site.remlit.blueb.aster.util.model.getMeta

object MetaRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			get("/api/meta") {
				call.respond(HttpStatusCode.OK, Meta.getMeta())
			}
		}
}
