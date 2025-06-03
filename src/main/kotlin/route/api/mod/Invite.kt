package site.remlit.blueb.route.api.mod

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import site.remlit.blueb.authenticatedUserKey
import site.remlit.blueb.db.entity.InviteEntity
import site.remlit.blueb.db.suspendTransaction
import site.remlit.blueb.model.ApiError
import site.remlit.blueb.service.IdentifierService
import site.remlit.blueb.service.InviteService
import site.remlit.blueb.service.RandomService
import site.remlit.blueb.service.TimelineService

private val identifierService = IdentifierService()
private val inviteService = InviteService()
private val randomService = RandomService()
private val timelineService = TimelineService()

fun Route.modInvite() {
	authenticate("authRequiredMod") {
		get("/api/mod/invites") {
			val since = timelineService.normalizeSince(call.parameters["since"])
			val take = timelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

			call.respond(HttpStatusCode.NotImplemented)
		}

		post("/api/mod/invite") {
			val authenticatedUser = call.attributes[authenticatedUserKey]

			val id = identifierService.generate()
			val code = randomService.generateString()

			suspendTransaction {
				InviteEntity.new(id) {
					this.code = code
					creator = authenticatedUser
				}
			}

			val invite = inviteService.getById(id)

			if (invite == null) {
				call.respond(
					HttpStatusCode.NotFound,
					ApiError(
						"Invite couldn't be found after creation",
						call.callId
					)
				)
				return@post
			}

			call.respond(invite)
		}

		delete("/api/mod/invite/{id}") {
			val invite = inviteService.getById(call.parameters.getOrFail("id"))

			if (invite == null) {
				call.respond(HttpStatusCode.NotFound)
				return@delete
			}

			inviteService.deleteById(invite.id)

			call.respond(HttpStatusCode.OK)
		}
	}
}
