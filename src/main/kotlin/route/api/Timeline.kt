package site.remlit.blueb.aster.route.api

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import site.remlit.blueb.aster.model.ApiException
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.service.TimelineService

fun Route.timeline() {
	val configuration = Configuration()

	val timelineService = TimelineService()

	authenticate("authRequired") {
		get("/api/timeline/home") {
			val since = timelineService.normalizeSince(call.parameters["since"])
			val take = timelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

			val local = call.request.queryParameters["local"]?.toBoolean() ?: true

			throw ApiException(HttpStatusCode.NotImplemented)
		}
	}

	authenticate(if (configuration.timeline.local.authRequired) "authRequired" else "authOptional") {
		get("/api/timeline/local") {
			val since = timelineService.normalizeSince(call.parameters["since"])
			val take = timelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

			throw ApiException(HttpStatusCode.NotImplemented)
		}
	}

	authenticate(if (configuration.timeline.bubble.authRequired) "authRequired" else "authOptional") {
		get("/api/timeline/bubble") {
			val since = timelineService.normalizeSince(call.parameters["since"])
			val take = timelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

			throw ApiException(HttpStatusCode.NotImplemented)
		}
	}

	authenticate(if (configuration.timeline.public.authRequired) "authRequired" else "authOptional") {
		get("/api/timeline/public") {
			val since = timelineService.normalizeSince(call.parameters["since"])
			val take = timelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

			throw ApiException(HttpStatusCode.NotImplemented)
		}
	}
}
