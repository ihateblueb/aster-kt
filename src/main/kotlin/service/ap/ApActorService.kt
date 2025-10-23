package site.remlit.blueb.aster.service.ap

import io.ktor.http.*
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.JsonObject
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory
import site.remlit.blueb.aster.db.entity.UserEntity
import site.remlit.blueb.aster.model.Service
import site.remlit.blueb.aster.service.FormatService
import site.remlit.blueb.aster.service.IdentifierService
import site.remlit.blueb.aster.service.ResolverService
import site.remlit.blueb.aster.service.SanitizerService
import site.remlit.blueb.aster.service.TimeService
import site.remlit.blueb.aster.service.UserService
import site.remlit.blueb.aster.util.ifFails

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
		 *
		 * @return UserEntity or null
		 * */
		suspend fun resolve(apId: String): UserEntity? {
			val existingUser = UserService.getByApId(apId)

			if (existingUser != null) {
				// TODO: update
				return existingUser
			}

			val resolveResponse = ResolverService.resolve(apId)

			if (resolveResponse != null)
				return register(resolveResponse)

			return null
		}

		/**
		 * Register a new actor
		 *
		 * @param json JSON representation of an actor
		 *
		 * @return UserEntity or null
		 * */
		fun register(json: JsonObject): UserEntity? {
			val id = IdentifierService.generate()

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

			try {
				transaction {
					UserEntity.new(id) {
						apId = extractedId
						this.inbox = inbox
						outbox = ApUtilityService.extractString(json["outbox"])

						username = SanitizerService.sanitize(extractedPreferredUsername, true)

						val extractedName = ApUtilityService.extractString(json["name"])
						displayName =
							if (!extractedName.isNullOrBlank()) SanitizerService.sanitize(extractedName, true) else null

						host = FormatService.toASCII(Url(extractedId).host)

						// todo: icon, image

						bio = if (summary != null) SanitizerService.sanitize(summary) else null

						sensitive = ApUtilityService.extractBoolean(json["sensitive"]) ?: false
						discoverable = ApUtilityService.extractBoolean(json["discoverable"]) ?: false
						locked = ApUtilityService.extractBoolean(json["manuallyApprovesFollowers"]) ?: false
						indexable = ApUtilityService.extractBoolean(json["noindex"])?.let { !it } ?: true
						isCat = ApUtilityService.extractBoolean(json["isCat"]) ?: false
						speakAsCat = ApUtilityService.extractBoolean(json["speakAsCat"]) ?: false

						activated = true

						// TODO: birthday, location

						followersUrl = followers
						followingUrl = following

						createdAt = published

						publicKey = SanitizerService.sanitize(extractedPublicKey, true)
					}
				}

				return UserService.getById(id)
			} catch (e: Exception) {
				logger.error(e.message, e)
				return null
			}
		}
	}
}
