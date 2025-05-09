package me.blueb.route.api

import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import me.blueb.model.repository.UserRepository
import me.blueb.model.entity.UserEntity

@Resource("/api/user/{id}")
class UserResource(
    val id: String,
)

fun Route.user() {
    val userRepository = UserRepository()

    get<UserResource> { res ->
        val user = userRepository.getById(res.id)

        if (user == null || !user.activated || user.suspended) {
            call.respond(HttpStatusCode.Companion.NotFound)
        }

        call.respond(user!!)
    }
}
