package site.remlit.blueb.aster.route.ap

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.WellKnown
import site.remlit.blueb.aster.model.WellKnownLink
import site.remlit.blueb.aster.route.RouteRegistry

object HostMetaRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			get("/.well-known/host-meta") {
				call.response.headers.append("Content-Type", "application/jrd+json")

				call.respond(
					status = HttpStatusCode.OK,
					message = WellKnown(
						links = listOf(
							WellKnownLink(
								rel = "lrdd",
								type = "application/jrd+json",
								href = Configuration.url.toString() + ".well-known/webfinger?resource={uri}",
							)
						)
					)
				)
			}
		}
}