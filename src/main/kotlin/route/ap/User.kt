package me.blueb.route.ap

import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import me.blueb.model.ApActor
import me.blueb.model.repository.UserRepository

@Resource("/user/{id}")
class ApUserResource(
    val id: String,
)

fun Route.apUser() {
    val userRepository = UserRepository()

    get<ApUserResource> { res ->
        val user = userRepository.getById(res.id)

        if (user == null || user.host != null || !user.activated || user.suspended) {
            call.respond(HttpStatusCode.Companion.NotFound)
        }

        call.respond(
            ApActor.fromUser(user!!),
        )
    }
}
