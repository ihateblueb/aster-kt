package site.remlit.blueb.aster.route.api

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.http.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import site.remlit.blueb.aster.db.entity.UserEntity
import site.remlit.blueb.aster.db.entity.UserPrivateEntity
import site.remlit.blueb.aster.db.suspendTransaction
import site.remlit.blueb.aster.model.*
import site.remlit.blueb.aster.service.*
import site.remlit.blueb.aster.service.ap.ApIdService

@Serializable
data class RegisterBody(
	val username: String,
	val password: String,
	val invite: String? = null,
)

fun Route.register() {
	val configuration = Configuration()

	val apIdService = ApIdService()

	val authService = AuthService()
	val identifierService = IdentifierService()
	val inviteService = InviteService()
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
					callId = call.callId
				)
			)
			return@post
		}

		if (configuration.registrations == InstanceRegistrationsType.Invite && body.invite == null) {
			call.respond(
				status = HttpStatusCode.BadRequest,
				message = ApiError(
					message = "Invite required",
					callId = call.callId
				)
			)
			return@post
		}

		if (configuration.registrations == InstanceRegistrationsType.Invite) {
			if (body.invite.isNullOrBlank())
				throw ApiException(HttpStatusCode.BadRequest, "Invite required")

			val invite = inviteService.getByCode(body.invite)

			if (invite == null || invite.user != null)
				throw ApiException(HttpStatusCode.BadRequest, "Invite invalid")
		}

		// todo: implement invite usage

		val username = formatService.toASCII(body.username)

		if (validationService.containsNonAlphanumeric(username))
			throw ApiException(HttpStatusCode.BadRequest, "Username contains non-alphanumeric characters")

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
				followingUrl = apIdService.renderFollowingApId(id)
				followersUrl = apIdService.renderFollowersApId(id)
				publicKey = keypairService.keyToPem(KeyType.Public, keypair)
			}
			UserPrivateEntity.new(id) {
				password = hashedPassword
				privateKey = keypairService.keyToPem(KeyType.Private, keypair)
			}
		}

		val user = userService.getById(id)

		if (user == null)
			throw ApiException(HttpStatusCode.NotFound, "User not found after creation")

		if (configuration.registrations == InstanceRegistrationsType.Invite)
			inviteService.useInvite(body.invite!!, user.id.toString())

		val token = authService.registerToken(user)

		call.respond(AuthResponse(token, User.fromEntity(user)))
	}
}
