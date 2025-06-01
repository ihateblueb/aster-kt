package site.remlit.blueb.route.api

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.http.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import site.remlit.blueb.db.entity.UserEntity
import site.remlit.blueb.db.entity.UserPrivateEntity
import site.remlit.blueb.db.suspendTransaction
import site.remlit.blueb.model.*
import site.remlit.blueb.service.*
import site.remlit.blueb.service.ap.ApIdService

@Serializable
data class RegisterBody(
	val username: String,
	val password: String,
	val invite: String? = null,
)

fun Route.register() {
	val configuration = Configuration()

	val apIdService = ApIdService()

	val identifierService = IdentifierService()
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
