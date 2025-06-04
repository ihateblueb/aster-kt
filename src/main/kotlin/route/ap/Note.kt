package site.remlit.blueb.route.ap

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import site.remlit.blueb.model.ApiException
import site.remlit.blueb.model.Visibility
import site.remlit.blueb.model.activity.ApCreateActivity
import site.remlit.blueb.model.ap.ApNote
import site.remlit.blueb.service.NoteService

fun Route.apNote() {
	val noteService = NoteService()

	get("/notes/{id}") {
		call.response.headers.append("Content-Type", "application/activity+json")

		val note = noteService.getById(call.parameters.getOrFail("id"))

		if (
			note == null ||
			!note.user.activated ||
			note.user.suspended ||
			!note.user.host.isNullOrBlank() ||
			(note.visibility != Visibility.Public &&
				note.visibility != Visibility.Unlisted)
		)
			throw ApiException(HttpStatusCode.NotFound)

		call.respond(ApNote.fromEntity(note))
	}

	get("/notes/{id}/activity") {
		call.response.headers.append("Content-Type", "application/activity+json")

		val note = noteService.getById(call.parameters.getOrFail("id"))

		if (
			note == null ||
			!note.user.activated ||
			note.user.suspended ||
			!note.user.host.isNullOrBlank() ||
			(note.visibility != Visibility.Public &&
				note.visibility != Visibility.Unlisted)
		)
			throw ApiException(HttpStatusCode.NotFound)

		call.respond(
			ApCreateActivity(
				actor = note.user.apId,
				`object` = ApNote.fromEntity(note)
			)
		)
	}
}
