package site.remlit.blueb.route.api

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import site.remlit.blueb.model.Configuration
import site.remlit.blueb.service.TimelineService

private val configuration = Configuration()

private val timelineService = TimelineService()

fun Route.timeline() {
	authenticate("authRequired") {
		get("/api/timeline/home") {
			val since = timelineService.normalizeSince(call.parameters["since"])
			val take = timelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

			val local = call.request.queryParameters["local"]?.toBoolean() ?: true

			call.respond(HttpStatusCode.NotImplemented)
		}
	}

	authenticate(if (configuration.timeline.local.authRequired) "authRequired" else "authOptional") {
		get("/api/timeline/local") {
			val since = timelineService.normalizeSince(call.parameters["since"])
			val take = timelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

			call.respond(HttpStatusCode.NotImplemented)
		}
	}

	authenticate(if (configuration.timeline.bubble.authRequired) "authRequired" else "authOptional") {
		get("/api/timeline/bubble") {
			val since = timelineService.normalizeSince(call.parameters["since"])
			val take = timelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

			call.respond(HttpStatusCode.NotImplemented)
		}
	}

	authenticate(if (configuration.timeline.public.authRequired) "authRequired" else "authOptional") {
		get("/api/timeline/public") {
			val since = timelineService.normalizeSince(call.parameters["since"])
			val take = timelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

			call.respond(HttpStatusCode.NotImplemented)
		}
	}
}
