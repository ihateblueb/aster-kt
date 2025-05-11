package me.blueb.route.api

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.callid.callId
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.Serializable
import me.blueb.db.entity.UserEntity
import me.blueb.db.entity.UserPrivateEntity
import me.blueb.model.ApiError
import me.blueb.model.InstanceRegistrationsMode
import me.blueb.model.Configuration
import me.blueb.service.IdentifierService
import me.blueb.service.UserService
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class RegisterBody(
    val username: String,
    val password: String,
    val invite: String? = null,
)

fun Route.register() {
    val configuration = Configuration()
    val identifierService = IdentifierService()
    val userService = UserService()

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
        val hashedPassword = BCrypt.withDefaults().hashToString(12, body.password.toCharArray())

        transaction {
            UserEntity.new(id) {
                apId = configuration.url.toString() + "user/" + id
                inbox = configuration.url.toString() + "user/" + id + "/inbox"
                outbox = configuration.url.toString() + "user/" + id + "/outbox"
                username = body.username
                activated = configuration.registrations != InstanceRegistrationsMode.Approval
            }
            UserPrivateEntity.new(id) {
                password = hashedPassword
            }
        }

        call.respond(userService.getById(id) as Any)
    }
}
