package me.blueb.route.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.callid.callId
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.util.getOrFail
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import me.blueb.authenticatedUserKey
import me.blueb.db.entity.NoteEntity
import me.blueb.db.suspendTransaction
import me.blueb.model.ApiError
import me.blueb.model.Configuration
import me.blueb.model.RoleType
import me.blueb.model.Visibility
import me.blueb.service.IdentifierService
import me.blueb.service.NoteService
import me.blueb.service.RoleService
import org.apache.commons.text.StringEscapeUtils

@Serializable
data class PostNoteBody(
	val cw: String? = null,
	val content: String? = null,
	val visibility: String,
)

fun Route.note() {
	val configuration = Configuration()

	val identifierService = IdentifierService()
	val noteService = NoteService()
	val roleService = RoleService()

	get("/api/note/{id}") {
		val note = noteService.getById(call.parameters.getOrFail("id"))

		if (
			note == null ||
			!note.user.activated ||
			note.user.suspended ||
			(note.visibility != Visibility.Public &&
				note.visibility != Visibility.Unlisted)
		) {
			call.respond(HttpStatusCode.NotFound)
			return@get
		}

		call.respond(note)
	}

	get("/api/note/{id}/replies") {
		call.respond(HttpStatusCode.NotImplemented)
	}

	authenticate("authRequired") {
		delete("/api/note/{id}") {
			val authenticatedUser = call.attributes[authenticatedUserKey]

			val note = noteService.getById(call.parameters.getOrFail("id"))

			if (note == null) {
				call.respond(HttpStatusCode.NotFound)
				return@delete
			}

			if (
				note.user.id != authenticatedUser.id.toString() && (!roleService.userHasRoleOfType(authenticatedUser.id.toString(),
					RoleType.Admin) || !roleService.userHasRoleOfType(authenticatedUser.id.toString(), RoleType.Mod))
			) {
				call.respond(HttpStatusCode.Forbidden)
				return@delete
			}

			noteService.deleteById(note.id.toString())

			call.respond(HttpStatusCode.OK)
		}

		patch("/api/note/{id}") {
			call.respond(HttpStatusCode.NotImplemented)
		}

		post("/api/note") {
			val authenticatedUser = call.attributes[authenticatedUserKey]

			val body = call.receive<PostNoteBody>()

			if (body.content.isNullOrBlank()) {
				call.respond(
					HttpStatusCode.BadRequest,
					ApiError(
						message = "Content is required",
						requestId = call.callId
					)
				)
				return@post
			}

			val id = identifierService.generate()

			suspendTransaction {
				NoteEntity.new(id) {
					apId = configuration.url.toString() + "note/" + id
					user = authenticatedUser
					cw = StringEscapeUtils.escapeHtml4(body.cw)
					content = StringEscapeUtils.escapeHtml4(body.content)
					visibility = Visibility.fromString(body.visibility)
					to = listOf()
					tags = listOf()
					createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
				}
			}

			call.respond(HttpStatusCode.NotImplemented)
		}

		post("/note/{id}/repeat") {
			call.respond(HttpStatusCode.NotImplemented)
		}

		post("/note/{id}/like") {
			call.respond(HttpStatusCode.NotImplemented)
		}

		post("/note/{id}/react") {
			call.respond(HttpStatusCode.NotImplemented)
		}

		post("/note/{id}/bookmark") {
			call.respond(HttpStatusCode.NotImplemented)
		}

		/* Hide post and all replies to it, use conversation to determine replies */
		post("/note/{id}/hide") {
			call.respond(HttpStatusCode.NotImplemented)
		}

		/* stop receiving notifications for this post, user conversation for this, too */
		post("/note/{id}/mute") {
			call.respond(HttpStatusCode.NotImplemented)
		}
	}
}
