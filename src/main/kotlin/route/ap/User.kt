package me.blueb.route.ap

import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.util.getOrFail
import me.blueb.model.ApActor
import me.blueb.model.repository.UserRepository

fun Route.apUser() {
    val userRepository = UserRepository()

    get("/user/{id}") {
        val user = userRepository.getById(
            id = call.parameters.getOrFail("id")
        )

        if (user == null || user.host != null || !user.activated || user.suspended) {
            call.respond(HttpStatusCode.Companion.NotFound)
        }

        call.respond(
            ApActor.fromUser(user!!),
        )
    }
}
