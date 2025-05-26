package me.blueb.route.ap

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.util.getOrFail
import me.blueb.model.Visibility
import me.blueb.model.activity.ApCreateActivity
import me.blueb.model.ap.ApNote
import me.blueb.service.NoteService

fun Route.apNote() {
	val noteService = NoteService()

	get("/notes/{id}") {
		call.response.headers.append("Content-Type", "application/activity+json")

		val note = noteService.getById(call.parameters.getOrFail("id"))

		if (
			note == null ||
			!note.user.activated ||
			note.user.suspended ||
			!note.user.host.isNullOrEmpty() ||
			(note.visibility != Visibility.Public &&
				note.visibility != Visibility.Unlisted)
		) {
			call.respond(HttpStatusCode.NotFound)
			return@get
		}

		call.respond(ApNote.fromEntity(note))
	}

	get("/notes/{id}/activity") {
		call.response.headers.append("Content-Type", "application/activity+json")

		val note = noteService.getById(call.parameters.getOrFail("id"))

		if (
			note == null ||
			!note.user.activated ||
			note.user.suspended ||
			!note.user.host.isNullOrEmpty() ||
			(note.visibility != Visibility.Public &&
				note.visibility != Visibility.Unlisted)
		) {
			call.respond(HttpStatusCode.NotFound)
			return@get
		}

		call.respond(
			ApCreateActivity(
				actor = note.user.apId,
				`object` = ApNote.fromEntity(note)
			)
		)
	}
}
