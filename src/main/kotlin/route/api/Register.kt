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
import site.remlit.blueb.aster.model.ApiError
import site.remlit.blueb.aster.model.ApiException
import site.remlit.blueb.aster.model.AuthResponse
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.InstanceRegistrationsType
import site.remlit.blueb.aster.model.KeyType
import site.remlit.blueb.aster.model.User
import site.remlit.blueb.aster.service.AuthService
import site.remlit.blueb.aster.service.FormatService
import site.remlit.blueb.aster.service.IdentifierService
import site.remlit.blueb.aster.service.InviteService
import site.remlit.blueb.aster.service.KeypairService
import site.remlit.blueb.aster.service.UserService
import site.remlit.blueb.aster.service.ValidationService
import site.remlit.blueb.aster.service.ap.ApIdService

@Serializable
data class RegisterBody(
	val username: String,
	val password: String,
	val invite: String? = null,
)

fun Route.register() {
	post("/api/register") {
		val body = call.receive<RegisterBody>()

		if (Configuration.registrations == InstanceRegistrationsType.Closed) {
			call.respond(
				status = HttpStatusCode.BadRequest,
				message = ApiError(
					message = "Registrations closed",
					callId = call.callId
				)
			)
			return@post
		}

		if (Configuration.registrations == InstanceRegistrationsType.Invite && body.invite == null) {
			call.respond(
				status = HttpStatusCode.BadRequest,
				message = ApiError(
					message = "Invite required",
					callId = call.callId
				)
			)
			return@post
		}

		if (Configuration.registrations == InstanceRegistrationsType.Invite) {
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
				activated = Configuration.registrations != InstanceRegistrationsType.Approval
				followingUrl = ApIdService.renderFollowingApId(id)
				followersUrl = ApIdService.renderFollowersApId(id)
				publicKey = KeypairService.keyToPem(KeyType.Public, keypair)
			}
			UserPrivateEntity.new(id) {
				password = hashedPassword
				privateKey = KeypairService.keyToPem(KeyType.Private, keypair)
			}
		}

		val user = User.fromEntity(
			UserService.getById(id)
				?: throw ApiException(HttpStatusCode.NotFound)
		)

		if (Configuration.registrations == InstanceRegistrationsType.Invite)
			InviteService.useInvite(body.invite!!, user.id)

		val token = AuthService.registerToken(user.id)

		call.respond(AuthResponse(token, user))
	}
}
