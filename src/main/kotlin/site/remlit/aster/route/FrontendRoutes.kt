package site.remlit.aster.route

import io.ktor.server.http.content.*
import site.remlit.aster.model.Configuration
import site.remlit.aster.registry.RouteRegistry

internal object FrontendRoutes {
	fun register() {
		RouteRegistry.registerRoute {
			if (Configuration.builtinFrontend)
				singlePageApplication {
					useResources = true
					filesPath = "frontend"
				}
		}
	}
}
