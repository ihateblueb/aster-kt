package me.blueb.route.api

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.Serializable
import me.blueb.model.InstanceRegistrationsMode
import me.blueb.model.entity.UserEntity
import me.blueb.model.entity.UserPrivateEntity
import me.blueb.model.repository.UserPrivateRepository
import me.blueb.model.repository.UserRepository
import me.blueb.service.ConfigService
import me.blueb.service.IdentifierService

@Serializable
data class RegisterBody(
    val username: String,
    val password: String,
    val invite: String?
)

fun Route.register() {
    val configService = ConfigService()
    val identifierService = IdentifierService()

    val userRepository = UserRepository()
    val userPrivateRepository = UserPrivateRepository()

    post("/api/register") {
        val body = call.receive<RegisterBody>()

        if (configService.registrations == InstanceRegistrationsMode.Closed)
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Registrations closed",
            )

        if (configService.registrations == InstanceRegistrationsMode.Invite)
            call.respond(
                status = HttpStatusCode.NotImplemented,
                message = "Invite system not implemented",
            )

        /*if (configService.registrations == InstanceRegistrationsMode.Invite && body.invite == null)
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Invite required",
            )*/

        val id = identifierService.generate()

        val newUser =
            UserEntity(
                id = id,
                apId = configService.url.toString() + "user/" + id,
                inbox = configService.url.toString() + "user/" + id + "/inbox",
                outbox = configService.url.toString() + "user/" + id + "/outbox",
                username = body.username,
                host = configService.url.host,
                activated = configService.registrations != InstanceRegistrationsMode.Approval,
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
    }
}
