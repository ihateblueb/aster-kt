package me.blueb.route.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.util.getOrFail
import me.blueb.model.repository.UserRepository

fun Route.user() {
    val userRepository = UserRepository()

    get("/api/user/{id}") {
        val user = userRepository.getById(
            id = call.parameters.getOrFail("id")
        )

        if (user == null || !user.activated || user.suspended) {
            call.respond(HttpStatusCode.Companion.NotFound)
        }

        call.respond(user!!)
    }
}
