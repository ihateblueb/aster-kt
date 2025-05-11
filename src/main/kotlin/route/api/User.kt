package me.blueb.route.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.util.getOrFail
import me.blueb.model.User
import me.blueb.service.UserService

fun Route.user() {
    val userService = UserService()

    get("/api/user/{id}") {
        val user = userService.getById(call.parameters.getOrFail("id"))

        if (user == null || !user.activated || user.suspended) {
            call.respond(HttpStatusCode.Companion.NotFound)
        }

        call.respond(
            User.fromEntity(user!!)
        )
    }
}
