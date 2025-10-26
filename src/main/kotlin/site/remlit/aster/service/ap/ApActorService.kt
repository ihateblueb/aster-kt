package site.remlit.aster.service.ap

import io.ktor.http.*
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.JsonObject
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory
import site.remlit.aster.common.model.User
import site.remlit.aster.common.model.generated.PartialUser
import site.remlit.aster.db.entity.UserEntity
import site.remlit.aster.model.Service
import site.remlit.aster.service.FormatService
import site.remlit.aster.service.IdentifierService
import site.remlit.aster.service.ResolverService
import site.remlit.aster.service.SanitizerService
import site.remlit.aster.service.TimeService
import site.remlit.aster.service.UserService
import site.remlit.aster.util.ifFails
import site.remlit.aster.util.model.fromEntity

/**
 * Service to handle ActivityPub actors.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
class ApActorService : Service() {
	companion object {
		private val logger = LoggerFactory.getLogger(ApActorService::class.java)

		/**
		 * Resolve an actor by their ID
		 *
		 * @param apId ActivityPub ID of an actor
		 * @param refetch Force a refetch
		 *
		 * @return UserEntity or null
		 * */
		suspend fun resolve(apId: String, refetch: Boolean = false): UserEntity? {
			val existingUser = UserService.getByApId(apId)

			if ((existingUser != null) && !refetch) {
				return existingUser
			}

			val resolveResponse = ResolverService.resolveSigned(apId)

			if (resolveResponse != null && existingUser == null)
				return register(toUser(resolveResponse) ?: return null)

			if (resolveResponse != null && existingUser != null)
				return update(toUser(resolveResponse, User.fromEntity(existingUser)) ?: return null)

			return null
		}

		// partials used here since a regular user has the expectation of being real,
		// may in future have calculated fields like likes on note, where creating them
		// would waste a query and potentially error
		fun toUser(json: JsonObject, existing: User? = null): PartialUser? {
			val extractedId = ApUtilityService.extractString(json["id"])

			if (extractedId.isNullOrBlank()) {
				logger.debug("Actor ID is null or blank")
				return null
			}

			val extractedType = ApUtilityService.extractString(json["type"])

			if (
				extractedType.isNullOrBlank() ||
				!listOf("Person", "Service").contains(extractedType)
			) {
				logger.debug("Actor type is null, is blank, or invalid")
				return null
			}

			val extractedPreferredUsername = ApUtilityService.extractString(json["preferredUsername"])

			if (extractedPreferredUsername.isNullOrBlank()) {
				logger.debug("Actor preferredUsername is null or blank")
				return null
			}

			val extractedSharedInbox = ApUtilityService.extractString(json["sharedInbox"])
			val extractedInbox = ApUtilityService.extractString(json["inbox"])

			val inbox = extractedSharedInbox ?: extractedInbox

			if (inbox == null || inbox.isBlank()) {
				logger.debug("Actor inbox is null or blank")
				return null
			}

			val extractedMisskeySummary = ApUtilityService.extractString(json["_misskey_summary"])
			val extractedSummary = ApUtilityService.extractString(json["summary"])

			val summary = extractedMisskeySummary ?: extractedSummary

			val extractedPublicKey = ApUtilityService.extractString(
				ApUtilityService.extractObject(json["publicKey"])?.get("publicKeyPem")
			)

			if (extractedPublicKey.isNullOrBlank()) {
				logger.debug("Actor public key is null or blank")
				return null
			}

			val followers = ApUtilityService.extractString(json["followers"])
			val following = ApUtilityService.extractString(json["following"])

			val published = ApUtilityService.extractString(json["published"]).let {
				if (it != null) ifFails({ LocalDateTime.parse(it) }) {
					TimeService.now()
				} else TimeService.now()
			}

			return PartialUser(
				id = existing?.id ?: IdentifierService.generate(),
				apId = existing?.apId ?: extractedId,

				username = SanitizerService.sanitize(extractedPreferredUsername, true),
				displayName = ApUtilityService.extractString(json["name"]),

				host = existing?.host ?: FormatService.toASCII(Url(extractedId).host),

				bio = if (summary != null) SanitizerService.sanitize(summary) else null,
				birthday = null,
				location = null,

				avatar = null,
				avatarAlt = null,
				banner = null,
				bannerAlt = null,

				inbox = inbox,
				outbox = ApUtilityService.extractString(json["outbox"]),

				activated = true,
				automated = extractedType != "Person",
				suspended = existing?.suspended ?: false,
				sensitive = ApUtilityService.extractBoolean(json["sensitive"]) ?: false,
				discoverable = ApUtilityService.extractBoolean(json["discoverable"]) ?: false,
				locked = ApUtilityService.extractBoolean(json["manuallyApprovesFollowers"]) ?: false,
				indexable = ApUtilityService.extractBoolean(json["noindex"])?.let { !it } ?: true,
				isCat = ApUtilityService.extractBoolean(json["isCat"]) ?: false,
				speakAsCat = ApUtilityService.extractBoolean(json["speakAsCat"]) ?: false,

				followersUrl = followers,
				followingUrl = following,

				createdAt = published,
				updatedAt = if (existing != null) TimeService.now() else null,

				publicKey = existing?.publicKey ?: SanitizerService.sanitize(extractedPublicKey, true),
			)
		}

		/**
		 * Update an existing actor
		 *
		 * @param user Converted actor to partial user
		 *
		 * @return UserEntity or null
		 * */
		fun update(user: PartialUser): UserEntity? {
			try {
				transaction {
					UserEntity.findByIdAndUpdate(user.id!!) {
						it.apId = user.apId!!

						it.username = user.username!!
						it.displayName = user.displayName

						it.host = user.host

						it.bio = user.bio
						it.birthday = user.birthday
						it.location = user.location

						it.avatar = user.avatar
						it.avatarAlt = user.avatarAlt
						it.banner = user.banner
						it.bannerAlt = user.bannerAlt

						it.inbox = user.inbox!!
						it.outbox = user.outbox

						it.activated = user.activated ?: true
						it.automated = user.automated ?: false
						it.suspended = user.suspended ?: false
						it.sensitive = user.sensitive ?: false
						it.discoverable = user.discoverable ?: false
						it.locked = user.locked ?: false
						it.indexable = user.indexable ?: false
						it.isCat = user.isCat ?: false
						it.speakAsCat = user.speakAsCat ?: false

						it.followersUrl = user.followersUrl
						it.followingUrl = user.followingUrl

						it.createdAt = user.createdAt ?: TimeService.now()
						it.updatedAt = user.updatedAt

						it.publicKey = user.publicKey!!
					}
				}

				return UserService.getById(user.id!!)
			} catch (e: Exception) {
				logger.error(e.message, e)
				return null
			}
		}

		/**
		 * Register a new actor
		 *
		 * @param user Converted actor to partial user
		 *
		 * @return UserEntity or null
		 * */
		fun register(user: PartialUser): UserEntity? {
			try {
				transaction {
					UserEntity.new(user.id) {
						this.apId = user.apId!!

						this.username = user.username!!
						this.displayName = user.displayName

						this.host = user.host

						this.bio = user.bio
						this.birthday = user.birthday
						this.location = user.location

						this.avatar = user.avatar
						this.avatarAlt = user.avatarAlt
						this.banner = user.banner
						this.bannerAlt = user.bannerAlt

						this.inbox = user.inbox!!
						this.outbox = user.outbox

						this.activated = user.activated ?: true
						this.automated = user.automated ?: false
						this.suspended = user.suspended ?: false
						this.sensitive = user.sensitive ?: false
						this.discoverable = user.discoverable ?: false
						this.locked = user.locked ?: false
						this.indexable = user.indexable ?: false
						this.isCat = user.isCat ?: false
						this.speakAsCat = user.speakAsCat ?: false

						this.followersUrl = user.followersUrl
						this.followingUrl = user.followingUrl

						this.createdAt = user.createdAt ?: TimeService.now()
						this.updatedAt = user.updatedAt

						this.publicKey = user.publicKey!!
					}
				}

				return UserService.getById(user.id!!)
			} catch (e: Exception) {
				logger.error(e.message, e)
				return null
			}
		}
	}
}
