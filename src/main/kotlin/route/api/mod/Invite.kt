package site.remlit.blueb.aster.route.api.mod

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import site.remlit.blueb.aster.authenticatedUserKey
import site.remlit.blueb.aster.db.entity.InviteEntity
import site.remlit.blueb.aster.db.suspendTransaction
import site.remlit.blueb.aster.model.ApiException
import site.remlit.blueb.aster.service.IdentifierService
import site.remlit.blueb.aster.service.InviteService
import site.remlit.blueb.aster.service.RandomService
import site.remlit.blueb.aster.service.TimelineService

fun Route.modInvite() {
	val identifierService = IdentifierService()
	val inviteService = InviteService()
	val randomService = RandomService()
	val timelineService = TimelineService()

	authenticate("authRequiredMod") {
		get("/api/mod/invites") {
			val since = timelineService.normalizeSince(call.parameters["since"])
			val take = timelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

			throw ApiException(HttpStatusCode.NotImplemented)
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

			if (invite == null)
				throw ApiException(HttpStatusCode.NotFound, "Not found after creation")

			call.respond(invite)
		}

		delete("/api/mod/invite/{id}") {
			val invite = inviteService.getById(call.parameters.getOrFail("id"))

			if (invite == null)
				throw ApiException(HttpStatusCode.NotFound)

			inviteService.deleteById(invite.id)

			call.respond(HttpStatusCode.OK)
		}
	}
}
