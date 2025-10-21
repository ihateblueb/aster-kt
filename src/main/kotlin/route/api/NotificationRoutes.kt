package site.remlit.blueb.aster.route.api

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.less
import site.remlit.blueb.aster.db.table.NoteTable
import site.remlit.blueb.aster.db.table.NotificationTable
import site.remlit.blueb.aster.route.RouteRegistry
import site.remlit.blueb.aster.service.NotificationService
import site.remlit.blueb.aster.service.TimelineService
import site.remlit.blueb.aster.util.authenticatedUserKey

object NotificationRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			authenticate("authRequired") {
				get("/api/notifications") {
					val authenticatedUser = call.attributes[authenticatedUserKey]
					val since = TimelineService.normalizeSince(call.parameters["since"])
					val take = TimelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

					val notifications = NotificationService.getMany(
						where = NotificationTable.to eq authenticatedUser.id.toString()
								and (NoteTable.createdAt less since),
						take = take
					)

					if (notifications.isEmpty()) {
						call.respond(HttpStatusCode.NoContent)
						return@get
					}

					call.respond(notifications)
				}

				delete("/api/notification/{id}") {
					call.respond(HttpStatusCode.NotImplemented)
				}
			}
		}
}