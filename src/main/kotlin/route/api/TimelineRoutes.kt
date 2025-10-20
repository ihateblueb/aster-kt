package site.remlit.blueb.aster.route.api

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.core.or
import site.remlit.blueb.aster.common.model.Visibility
import site.remlit.blueb.aster.db.table.NoteTable
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.model.ApiException
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.route.RouteRegistry
import site.remlit.blueb.aster.service.NoteService
import site.remlit.blueb.aster.service.TimelineService

object TimelineRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			authenticate("authRequired") {
				get("/api/timeline/home") {
					val since = TimelineService.normalizeSince(call.parameters["since"])
					val take = TimelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

					val local = call.request.queryParameters["local"]?.toBoolean() ?: true

					throw ApiException(HttpStatusCode.NotImplemented)
				}
			}

			authenticate(if (Configuration.timeline.local.authRequired) "authRequired" else "authOptional") {
				get("/api/timeline/local") {
					val since = TimelineService.normalizeSince(call.parameters["since"])
					val take = TimelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

					val notes = NoteService.getMany(
						where = NoteTable.visibility inList listOf(Visibility.Public, Visibility.Unlisted)
								and (UserTable.host eq null)
								and (NoteTable.createdAt less since),
						take = take
					)

					if (notes.isEmpty()) {
						call.respond(HttpStatusCode.NoContent)
						return@get
					}

					call.respond(notes)
				}
			}

			authenticate(if (Configuration.timeline.bubble.authRequired) "authRequired" else "authOptional") {
				get("/api/timeline/bubble") {
					val since = TimelineService.normalizeSince(call.parameters["since"])
					val take = TimelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

					val hosts = Configuration.timeline.bubble.hosts

					val notes = NoteService.getMany(
						where = NoteTable.visibility inList listOf(Visibility.Public, Visibility.Unlisted)
								and (UserTable.host inList hosts or (UserTable.host eq null))
								and (NoteTable.createdAt less since),
						take = take
					)

					if (notes.isEmpty()) {
						call.respond(HttpStatusCode.NoContent)
						return@get
					}
					
					call.respond(notes)
				}
			}

			authenticate(if (Configuration.timeline.public.authRequired) "authRequired" else "authOptional") {
				get("/api/timeline/public") {
					val since = TimelineService.normalizeSince(call.parameters["since"])
					val take = TimelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

					val notes = NoteService.getMany(
						NoteTable.visibility inList listOf(Visibility.Public, Visibility.Unlisted)
								and (NoteTable.createdAt less since),
						take
					)

					// todo: implement hideRemoteContent, making this essentially local tl 2

					if (notes.isEmpty()) {
						call.respond(HttpStatusCode.NoContent)
						return@get
					}

					call.respond(notes)
				}
			}
		}
}