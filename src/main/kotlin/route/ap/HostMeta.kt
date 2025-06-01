package site.remlit.blueb.route.ap

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import site.remlit.blueb.model.Configuration
import site.remlit.blueb.model.WellKnown
import site.remlit.blueb.model.WellKnownLink

fun Route.hostMeta() {
	val configuration = Configuration()

	get("/.well-known/host-meta") {
		call.response.headers.append("Content-Type", "application/jrd+json")

		call.respond(
			status = HttpStatusCode.OK,
			message = WellKnown(
				links = listOf(
					WellKnownLink(
						rel = "lrdd",
						type = "application/jrd+json",
						href = configuration.url.toString() + ".well-known/webfinger?resource={uri}",
					)
				)
			)
		)
	}
}
