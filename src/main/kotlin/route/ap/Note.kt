package site.remlit.blueb.aster.route.ap

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import site.remlit.blueb.aster.model.ApiException
import site.remlit.blueb.aster.model.Visibility
import site.remlit.blueb.aster.model.ap.ApNote
import site.remlit.blueb.aster.model.ap.activity.ApCreateActivity
import site.remlit.blueb.aster.service.NoteService

fun Route.apNote() {
	get("/notes/{id}") {
		call.response.headers.append("Content-Type", "application/activity+json")

		val note = NoteService.getById(call.parameters.getOrFail("id"))

		if (
			note == null ||
			! note.user.activated ||
			note.user.suspended ||
			! note.user.host.isNullOrBlank() ||
			(note.visibility != Visibility.Public &&
				note.visibility != Visibility.Unlisted)
		)
			throw ApiException(HttpStatusCode.NotFound)

		call.respond(ApNote.fromEntity(note))
	}

	get("/notes/{id}/activity") {
		call.response.headers.append("Content-Type", "application/activity+json")

		val note = NoteService.getById(call.parameters.getOrFail("id"))

		if (
			note == null ||
			! note.user.activated ||
			note.user.suspended ||
			! note.user.host.isNullOrBlank() ||
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
