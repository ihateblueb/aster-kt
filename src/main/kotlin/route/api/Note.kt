package site.remlit.blueb.route.api

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable
import site.remlit.blueb.authenticatedUserKey
import site.remlit.blueb.db.entity.NoteEntity
import site.remlit.blueb.db.suspendTransaction
import site.remlit.blueb.model.*
import site.remlit.blueb.service.*
import site.remlit.blueb.service.ap.ApIdService

@Serializable
data class PostNoteBody(
	val cw: String? = null,
	val content: String? = null,
	val visibility: String,
)

fun Route.note() {
	Configuration()

	val identifierService = IdentifierService()
	val apIdService = ApIdService()
	val noteService = NoteService()
	val roleService = RoleService()
	TimeService()
	val sanitizerService = SanitizerService()

	get("/api/note/{id}") {
		val note = noteService.getById(call.parameters.getOrFail("id"))

		if (
			note == null ||
			!note.user.activated ||
			note.user.suspended ||
			(note.visibility != Visibility.Public &&
				note.visibility != Visibility.Unlisted)
		)
			throw ApiException(HttpStatusCode.NotFound, "Note not found.")

		call.respond(note)
	}

	get("/api/note/{id}/replies") {
		throw ApiException(HttpStatusCode.NotImplemented)
	}

	authenticate("authRequired") {
		delete("/api/note/{id}") {
			val authenticatedUser = call.attributes[authenticatedUserKey]

			val note = noteService.getById(call.parameters.getOrFail("id"))

			if (note == null)
				throw ApiException(HttpStatusCode.NotFound, "Note not found.")

			if (
				note.user.id != authenticatedUser.id.toString() && (!roleService.userHasRoleOfType(
					authenticatedUser.id.toString(),
					RoleType.Admin
				) || !roleService.userHasRoleOfType(authenticatedUser.id.toString(), RoleType.Mod))
			)
				throw ApiException(HttpStatusCode.Forbidden, "You don't have permission to delete this.")

			noteService.deleteById(note.id)

			call.respond(HttpStatusCode.OK)
		}

		patch("/api/note/{id}") {
			throw ApiException(HttpStatusCode.NotImplemented)
		}

		post("/api/note") {
			val authenticatedUser = call.attributes[authenticatedUserKey]

			val body = call.receive<PostNoteBody>()

			if (body.content.isNullOrBlank())
				throw ApiException(HttpStatusCode.BadRequest, "Content required")

			val id = identifierService.generate()

			suspendTransaction {
				NoteEntity.new(id) {
					apId = apIdService.renderNoteApId(id)
					user = authenticatedUser
					cw = if (body.cw != null) sanitizerService.sanitize(body.cw, true) else null
					content = sanitizerService.sanitize(body.content, true)
					visibility = Visibility.fromString(body.visibility)
					to = listOf()
					tags = listOf()
				}
			}

			val note = noteService.getById(id)

			if (note == null) {
				call.respond(
					HttpStatusCode.NotFound,
					ApiError(
						"Note couldn't be found after creation",
						call.callId
					)
				)
				return@post
			}

			call.respond(note)
		}

		post("/note/{id}/repeat") {
			throw ApiException(HttpStatusCode.NotImplemented)
		}

		post("/note/{id}/like") {
			throw ApiException(HttpStatusCode.NotImplemented)
		}

		post("/note/{id}/react") {
			throw ApiException(HttpStatusCode.NotImplemented)
		}

		post("/note/{id}/bookmark") {
			throw ApiException(HttpStatusCode.NotImplemented)
		}

		/* Hide post and all replies to it, use conversation to determine replies */
		post("/note/{id}/hide") {
			throw ApiException(HttpStatusCode.NotImplemented)
		}

		/* stop receiving notifications for this post, user conversation for this, too */
		post("/note/{id}/mute") {
			throw ApiException(HttpStatusCode.NotImplemented)
		}
	}
}
