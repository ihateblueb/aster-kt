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
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.service.ap.ApIdService
import site.remlit.blueb.aster.util.BCRYPT_COST

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

		val existingUsers = UserService.getMany(
			UserTable.host eq null
		)

		if (existingUsers.find { it.username.toLowerCase() == username } != null)
			throw ApiException(HttpStatusCode.BadRequest, "Username is already in use")

		val id = IdentifierService.generate()
		val hashedPassword = BCrypt.withDefaults().hashToString(BCRYPT_COST, body.password.toCharArray())

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

		val user = User.fromEntity(
			UserService.getById(id)
				?: throw ApiException(HttpStatusCode.NotFound)
		)

		if (configuration.registrations == InstanceRegistrationsType.Invite)
			InviteService.useInvite(body.invite!!, user.id)

		val token = AuthService.registerToken(user.id)

		call.respond(AuthResponse(token, user))
	}
}
