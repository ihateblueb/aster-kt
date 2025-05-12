package me.blueb.route.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.callid.callId
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.login() {
    post("/api/login") {
		call.respond(HttpStatusCode.NotImplemented)
    }
}