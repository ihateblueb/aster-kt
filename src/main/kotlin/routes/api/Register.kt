package me.blueb.routes.api

import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.resources.Resource
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import me.blueb.models.exposed.ExposedUser
import me.blueb.models.exposed.ExposedUserPrivate
import me.blueb.services.ConfigService
import me.blueb.services.IdentifierService
import org.koin.ktor.ext.inject

@Resource("/api/register")
class RegisterResource(val username: String, val password: String)

fun Route.register() {
    val configService by inject<ConfigService>()
    val identifierService by inject<IdentifierService>()

    post<RegisterResource> { res ->
        val id = identifierService.generate()

        val newUser = ExposedUser(
            id = id,
            apId = configService.instance.url.fullPath + "/user/" + id,
            inbox = configService.instance.url.fullPath + "/user/" + id + "/inbox",
            outbox = configService.instance.url.fullPath + "/user/" + id + "/outbox",
            username = res.username,
            host = configService.instance.url.host
        )

        val newUserPrivate = ExposedUserPrivate(
            id = id,
            password = res.password,
        )

        call.respond(HttpStatusCode.Companion.NotImplemented)
    }
}