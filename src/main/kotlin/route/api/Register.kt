package me.blueb.route.api

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.callid.callId
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.Serializable
import me.blueb.model.ApiError
import me.blueb.model.InstanceRegistrationsMode
import me.blueb.model.entity.UserEntity
import me.blueb.model.entity.UserPrivateEntity
import me.blueb.model.repository.UserPrivateRepository
import me.blueb.model.repository.UserRepository
import me.blueb.model.Configuration
import me.blueb.service.IdentifierService

@Serializable
data class RegisterBody(
    val username: String,
    val password: String,
    val invite: String? = null,
)

fun Route.register() {
    val configuration = Configuration()
    val identifierService = IdentifierService()

    val userRepository = UserRepository()
    val userPrivateRepository = UserPrivateRepository()

    post("/api/register") {
        val body = call.receive<RegisterBody>()

        if (configuration.registrations == InstanceRegistrationsMode.Closed) {
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = ApiError(
                    message = "Registrations closed",
                    requestId = call.callId
                )
            )
            return@post
        }

        if (configuration.registrations == InstanceRegistrationsMode.Invite) {
            call.respond(
                status = HttpStatusCode.NotImplemented,
                message = ApiError(
                    message = "Invite system not implemented",
                    requestId = call.callId
                )
            )
            return@post
        }

        /*if (configService.registrations == InstanceRegistrationsMode.Invite && body.invite == null)
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Invite required",
            )*/

        val id = identifierService.generate()

        val newUser =
            UserEntity(
                id = id,
                apId = configuration.url.toString() + "user/" + id,
                inbox = configuration.url.toString() + "user/" + id + "/inbox",
                outbox = configuration.url.toString() + "user/" + id + "/outbox",
                username = body.username,
                host = configuration.url.host,
                activated = configuration.registrations != InstanceRegistrationsMode.Approval,
            )

        val hashedPassword = BCrypt.withDefaults().hashToString(12, body.password.toCharArray())

        val newUserPrivate =
            UserPrivateEntity(
                id = id,
                password = hashedPassword,
            )

        userRepository.create(newUser)
        userPrivateRepository.create(newUserPrivate)

        val registeredUser = userRepository.getById(newUser.id)

        call.respond(registeredUser as Any)
        return@post
    }
}
