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
import me.blueb.db.suspendTransaction
import me.blueb.model.ApiError
import me.blueb.model.InstanceRegistrationsType
import me.blueb.model.Configuration
import me.blueb.model.KeyType
import me.blueb.model.User
import me.blueb.service.FormatService
import me.blueb.service.IdentifierService
import me.blueb.service.KeypairService
import me.blueb.service.UserService
import me.blueb.service.ValidationService
import me.blueb.service.ap.ApIdService

@Serializable
data class RegisterBody(
    val username: String,
    val password: String,
    val invite: String? = null,
)

fun Route.register() {
    val configuration = Configuration()

    val identifierService = IdentifierService()
	val apIdService = ApIdService()
    val userService = UserService()
	val keypairService = KeypairService()
	val validationService = ValidationService()
	val formatService = FormatService()

    post("/api/register") {
        val body = call.receive<RegisterBody>()

        if (configuration.registrations == InstanceRegistrationsType.Closed) {
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = ApiError(
                    message = "Registrations closed",
                    requestId = call.callId
                )
            )
            return@post
        }

        if (configuration.registrations == InstanceRegistrationsType.Invite) {
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

		val username = formatService.toASCII(body.username)

		if (validationService.containsNonAlphanumeric(username)) {
			call.respond(
				status = HttpStatusCode.BadRequest,
				message = ApiError(
					message = "Username contains non-alphanumeric characters",
					requestId = call.callId
				)
			)
			return@post
		}

        // todo: check if username is used
		// ilike?
		// 		val existingUser = userService.get(
		//			UserTable.username like body.username.lowercase()
		//		)
		//
		//		if (existingUser != null) {
		//			call.respond(
		//				status = HttpStatusCode.BadRequest,
		//				message = ApiError(
		//					message = "Username taken",
		//					requestId = call.callId
		//				)
		//			)
		//			return@post
		//		}

        val id = identifierService.generate()
        val hashedPassword = BCrypt.withDefaults().hashToString(12, body.password.toCharArray())

		val keypair = keypairService.generate()

		suspendTransaction {
 			UserEntity.new(id) {
				apId = apIdService.renderUserApId(id)
				inbox = apIdService.renderInboxApId(id)
				outbox = apIdService.renderOutboxApId(id)
				this.username = username
				activated = configuration.registrations != InstanceRegistrationsType.Approval
				publicKey = keypairService.keyToPem(KeyType.Public, keypair)
			}
			UserPrivateEntity.new(id) {
				password = hashedPassword
				privateKey = keypairService.keyToPem(KeyType.Private, keypair)
			}
		}

		val user = userService.getById(id)

		if (user == null) {
			call.respond(HttpStatusCode.NotFound)
			return@post
		}

        call.respond(User.fromEntity(user))
    }
}
