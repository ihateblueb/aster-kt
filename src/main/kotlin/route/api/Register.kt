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

			val invite = InviteService.getByCode(body.invite)

			if (invite == null || invite.user != null)
				throw ApiException(HttpStatusCode.BadRequest, "Invite invalid")
		}

		// todo: implement invite usage

		val username = FormatService.toASCII(body.username)

		if (ValidationService.containsNonAlphanumeric(username))
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

		val id = IdentifierService.generate()
		val hashedPassword = BCrypt.withDefaults().hashToString(12, body.password.toCharArray())

		val keypair = KeypairService.generate()

		suspendTransaction {
			UserEntity.new(id) {
				apId = ApIdService.renderUserApId(id)
				inbox = ApIdService.renderInboxApId(id)
				outbox = ApIdService.renderOutboxApId(id)
				this.username = username
				activated = configuration.registrations != InstanceRegistrationsType.Approval
				followingUrl = ApIdService.renderFollowingApId(id)
				followersUrl = ApIdService.renderFollowersApId(id)
				publicKey = KeypairService.keyToPem(KeyType.Public, keypair)
			}
			UserPrivateEntity.new(id) {
				password = hashedPassword
				privateKey = KeypairService.keyToPem(KeyType.Private, keypair)
			}
		}

		val user = UserService.getById(id)

		if (user == null)
			throw ApiException(HttpStatusCode.NotFound, "User not found after creation")

		if (configuration.registrations == InstanceRegistrationsType.Invite)
			InviteService.useInvite(body.invite!!, user.id.toString())

		val token = AuthService.registerToken(user)

		call.respond(AuthResponse(token, User.fromEntity(user)))
	}
}
