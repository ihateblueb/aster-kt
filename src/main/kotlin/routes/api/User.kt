package me.blueb.routes.api

import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import me.blueb.models.ExposedUser
import me.blueb.services.UserService
import org.koin.ktor.ext.inject

@Resource("/api/user/{id}")
class UserResource(val id: String)

fun Route.user() {
    val userService by inject<UserService>()

    get<UserResource> { res ->
        val user = userService.read(res.id)

        if (user == null || !user.activated || user.suspended)
            call.respond(HttpStatusCode.Companion.NotFound)

        call.respond<ExposedUser>(user!!)
    }
}