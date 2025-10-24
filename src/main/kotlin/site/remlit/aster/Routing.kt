package site.remlit.aster

import io.ktor.server.application.*
import io.ktor.server.routing.*
import site.remlit.aster.route.RouteRegistry

fun Application.configureRouting() {
	RouteRegistry.registerBuiltinRoutes()

	routing {
		// swaggerUI(path = "swagger", swaggerFile = "openapi.yaml")
		RouteRegistry.installRoutes(this)
	}
}
