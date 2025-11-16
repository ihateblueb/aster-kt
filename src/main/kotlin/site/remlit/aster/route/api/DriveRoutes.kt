package site.remlit.aster.route.api

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.less
import site.remlit.aster.db.table.DriveFileTable
import site.remlit.aster.db.table.UserTable
import site.remlit.aster.registry.RouteRegistry
import site.remlit.aster.service.DriveService
import site.remlit.aster.service.TimelineService
import site.remlit.aster.util.authenticatedUserKey

object DriveRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			authenticate("authRequired") {
				get("/api/drive") {
					val authenticatedUser = call.attributes[authenticatedUserKey]
					val since = TimelineService.normalizeSince(call.parameters["since"])
					val take = TimelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

					val files = DriveService.getMany(
						where = UserTable.id eq authenticatedUser.id.toString()
								and (DriveFileTable.createdAt less since),
						take = take
					)

					if (files.isEmpty()) {
						call.respond(HttpStatusCode.NoContent)
						return@get
					}

					call.respond(files)
				}
			}
		}
}