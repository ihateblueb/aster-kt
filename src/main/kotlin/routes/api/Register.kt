package me.blueb.routes.api

import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.resources.Resource
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.Serializable
import me.blueb.models.exposed.ExposedUser
import me.blueb.models.exposed.ExposedUserPrivate
import me.blueb.services.ConfigService
import me.blueb.services.IdentifierService
import org.koin.ktor.ext.inject
import kotlin.math.log

@Resource("/api/register")
class RegisterResource()

@Serializable
data class RegisterBody(
    val username: String,
    val password: String,
)

fun Route.register() {
    val configService by inject<ConfigService>()
    val identifierService by inject<IdentifierService>()

    post<RegisterResource> { res ->
        val body = call.receive<RegisterBody>()

        val id = identifierService.generate()

        println(body.username)

        val newUser =
            ExposedUser(
                id = id,
                apId = configService.instance.url.fullPath + "/user/" + id,
                inbox = configService.instance.url.fullPath + "/user/" + id + "/inbox",
                outbox = configService.instance.url.fullPath + "/user/" + id + "/outbox",
                username = body.username,
                host = configService.instance.url.host,
            )

        val newUserPrivate =
            ExposedUserPrivate(
                id = id,
                password = body.password,
            )

        call.respond(HttpStatusCode.Companion.NotImplemented)
    }
}
