package me.blueb.routes.ap

import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import me.blueb.models.ApActor
import me.blueb.services.UserService
import org.koin.ktor.ext.inject

@Resource("/actor/{id}")
class ApActorResource(
    val id: String,
)

fun Route.actor() {
    val userService by inject<UserService>()

    get<ApActorResource> { res ->
        val user = userService.read(res.id)

        if (user == null || user.host != null || !user.activated || user.suspended) {
            call.respond(HttpStatusCode.Companion.NotFound)
        }

        call.respond(
            ApActor.fromUser(user!!),
        )
    }
}
