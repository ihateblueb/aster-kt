package site.remlit.blueb.aster.route.api

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.and
import site.remlit.blueb.aster.db.table.NoteTable
import site.remlit.blueb.aster.model.ApiException
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.Visibility
import site.remlit.blueb.aster.service.NoteService
import site.remlit.blueb.aster.service.TimelineService

fun Route.timeline() {
	val configuration = Configuration()

	authenticate("authRequired") {
		get("/api/timeline/home") {
			val since = TimelineService.normalizeSince(call.parameters["since"])
			val take = TimelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

			val local = call.request.queryParameters["local"]?.toBoolean() ?: true

			throw ApiException(HttpStatusCode.NotImplemented)
		}
	}

	authenticate(if (configuration.timeline.local.authRequired) "authRequired" else "authOptional") {
		get("/api/timeline/local") {
			val since = TimelineService.normalizeSince(call.parameters["since"])
			val take = TimelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

			val notes = NoteService.getMany(
				NoteTable.visibility inList listOf(Visibility.Public, Visibility.Unlisted)
						and (NoteTable.user eq null)
						and (NoteTable.createdAt less since),
				take
			)

			if (notes.isEmpty()) {
				call.respond(HttpStatusCode.NoContent)
				return@get
			}

			call.respond(notes)
		}
	}

	authenticate(if (configuration.timeline.bubble.authRequired) "authRequired" else "authOptional") {
		get("/api/timeline/bubble") {
			val since = TimelineService.normalizeSince(call.parameters["since"])
			val take = TimelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

			throw ApiException(HttpStatusCode.NotImplemented)
		}
	}

	authenticate(if (configuration.timeline.public.authRequired) "authRequired" else "authOptional") {
		get("/api/timeline/public") {
			val since = TimelineService.normalizeSince(call.parameters["since"])
			val take = TimelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

			val notes = NoteService.getMany(
				NoteTable.visibility inList listOf(Visibility.Public, Visibility.Unlisted)
						and (NoteTable.createdAt less since),
				take
			)

			if (notes.isEmpty()) {
				call.respond(HttpStatusCode.NoContent)
				return@get
			}

			call.respond(notes)
		}
	}
}
