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

			suspendTransaction {
				InviteEntity.new(id) {
					this.code = code
					creator = authenticatedUser
				}
			}

			val invite = InviteService.getById(id)

			if (invite == null)
				throw ApiException(HttpStatusCode.NotFound, "Not found after creation")

			call.respond(invite)
		}

		delete("/api/mod/invite/{id}") {
			val invite = InviteService.getById(call.parameters.getOrFail("id"))

			if (invite == null)
				throw ApiException(HttpStatusCode.NotFound)

			InviteService.deleteById(invite.id)

			call.respond(HttpStatusCode.OK)
		}
	}
}
