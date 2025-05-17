package me.blueb.route.api.admin

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post

fun Route.adminPolicy() {
	get("/api/admin/policies") {
		call.respond(HttpStatusCode.NotImplemented)
	}

	post("/api/admin/policy") {
		call.respond(HttpStatusCode.NotImplemented)
	}

	patch("/api/admin/policy/{id}") {
		call.respond(HttpStatusCode.NotImplemented)
	}

	delete("/api/admin/policy/{id}") {
		call.respond(HttpStatusCode.NotImplemented)
	}
}
