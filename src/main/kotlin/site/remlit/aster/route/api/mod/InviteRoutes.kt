package site.remlit.aster.route.api.mod

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import site.remlit.aster.db.entity.InviteEntity
import site.remlit.aster.model.ApiException
import site.remlit.aster.route.RouteRegistry
import site.remlit.aster.service.IdentifierService
import site.remlit.aster.service.InviteService
import site.remlit.aster.service.RandomService
import site.remlit.aster.service.TimelineService
import site.remlit.aster.util.authenticatedUserKey

object InviteRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			authenticate("authRequiredMod") {
				get("/api/mod/invites") {
					val since = TimelineService.normalizeSince(call.parameters["since"])
					val take = TimelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

					throw ApiException(HttpStatusCode.NotImplemented)
				}

				post("/api/mod/invite") {
					val authenticatedUser = call.attributes[authenticatedUserKey]

					val id = IdentifierService.generate()
					val code = RandomService.generateString()

					transaction {
						InviteEntity.new(id) {
							this.code = code
							creator = authenticatedUser
						}
					}

					val invite =
						InviteService.getById(id) ?: throw ApiException(
							HttpStatusCode.NotFound,
							"Not found after creation"
						)

					call.respond(invite)
				}

				delete("/api/mod/invite/{id}") {
					val invite =
						InviteService.getById(call.parameters.getOrFail("id"))
							?: throw ApiException(HttpStatusCode.NotFound)

					InviteService.deleteById(invite.id)

					call.respond(HttpStatusCode.OK)
				}
			}
		}
}
