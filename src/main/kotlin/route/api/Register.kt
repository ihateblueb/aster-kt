package me.blueb.route.api

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.callid.callId
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import me.blueb.db.entity.UserEntity
import me.blueb.db.entity.UserPrivateEntity
import me.blueb.db.suspendTransaction
import me.blueb.db.table.UserTable
import me.blueb.model.ApiError
import me.blueb.model.InstanceRegistrationsType
import me.blueb.model.Configuration
import me.blueb.model.KeyType
import me.blueb.model.User
import me.blueb.service.IdentifierService
import me.blueb.service.KeypairService
import me.blueb.service.UserService
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
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
	val keypairService = KeypairService()

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
				apId = configuration.url.toString() + "user/" + id
				inbox = configuration.url.toString() + "user/" + id + "/inbox"
				outbox = configuration.url.toString() + "user/" + id + "/outbox"
				username = body.username
				activated = configuration.registrations != InstanceRegistrationsType.Approval
				publicKey = keypairService.keyToPem(KeyType.Public, keypair)
				createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
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
