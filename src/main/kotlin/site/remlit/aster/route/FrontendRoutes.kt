package site.remlit.aster.route

import io.ktor.server.http.content.*
import site.remlit.aster.model.Configuration

object FrontendRoutes {
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