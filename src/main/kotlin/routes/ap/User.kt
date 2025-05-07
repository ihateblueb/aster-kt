package me.blueb.routes.ap

import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import me.blueb.services.UserService
import org.koin.ktor.ext.inject

@Resource("/user/{id}")
class ApUserResource(val id: String?)

fun Route.apUser() {
    val userService by inject<UserService>()

    get<ApUserResource> { res ->
        call.respond(HttpStatusCode.Companion.NotImplemented)
    }
}