package me.blueb.service.ap

import io.ktor.http.Url
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.JsonObject
import me.blueb.db.entity.UserEntity
import me.blueb.db.suspendTransaction
import me.blueb.service.FormatService
import me.blueb.service.IdentifierService
import me.blueb.service.ResolverService
import me.blueb.service.SanitizerService
import me.blueb.service.TimeService
import me.blueb.service.UserService
import org.slf4j.LoggerFactory
import java.time.format.DateTimeParseException

class ApActorService {
	private val logger = LoggerFactory.getLogger(this::class.java)

	private val apUtilityService = ApUtilityService()

	private val identifierService = IdentifierService()
	private val resolverService = ResolverService()
	private val userService = UserService()
	private val timeService = TimeService()
	private val formatService = FormatService()
	private val sanitizerService = SanitizerService()

	/**
	 * Resolve an ActivityPub Actor by their ID
	 *
	 * @return [UserEntity] or null
	 * */
	suspend fun resolve(apId: String): UserEntity? {
		val existingUser = userService.getByApId(apId)

		if (existingUser != null) {
			// TODO: update
			return existingUser
		}

		val resolveResponse = resolverService.resolve(apId)

		if (resolveResponse != null) {
			return register(resolveResponse)
		}

		return null
	}

	suspend fun register(json: JsonObject): UserEntity? {
		val id = identifierService.generate()

		val extractedId = apUtilityService.extractString(json["id"])

		if (extractedId.isNullOrBlank()) {
			logger.debug("Actor ID is null or blank")
			return null
		}

		val extractedType = apUtilityService.extractString(json["type"])

		if (
			extractedType.isNullOrBlank() ||
			!listOf("Person", "Service").contains(extractedType)
		) {
			logger.debug("Actor type is null, is blank, or invalid")
			return null
		}

		val extractedPreferredUsername = apUtilityService.extractString(json["preferredUsername"])

		if (extractedPreferredUsername.isNullOrBlank()) {
			logger.debug("Actor preferredUsername is null or blank")
			return null
		}

		val extractedSharedInbox = apUtilityService.extractString(json["sharedInbox"])
		val extractedInbox = apUtilityService.extractString(json["inbox"])

		val inbox = extractedSharedInbox ?: extractedInbox

		if (inbox == null || inbox.isBlank()) {
			logger.debug("Actor inbox is null or blank")
			return null
		}

		val extractedMisskeySummary = apUtilityService.extractString(json["_misskey_summary"])
		val extractedSummary = apUtilityService.extractString(json["summary"])

		val summary = extractedMisskeySummary ?: extractedSummary

		val extractedPublicKey = apUtilityService.extractString(
			apUtilityService.extractObject(json["publicKey"])?.get("publicKeyPem")
		)

		if (extractedPublicKey.isNullOrBlank()) {
			logger.debug("Actor public key is null or blank")
			return null
		}

		try {
			suspendTransaction {
				UserEntity.new(id) {
					apId = extractedId
					this.inbox = inbox
					outbox = apUtilityService.extractString(json["outbox"])

					username = sanitizerService.sanitize(extractedPreferredUsername, true)

					val extractedName = apUtilityService.extractString(json["name"])
					displayName = if (!extractedName.isNullOrBlank()) sanitizerService.sanitize(extractedName, true) else null

					host = formatService.toASCII(Url(extractedId).host)

					// todo: icon, image

					bio = if (summary != null) sanitizerService.sanitize(summary) else null

					sensitive = apUtilityService.extractBoolean(json["sensitive"]) ?: false
					discoverable = apUtilityService.extractBoolean(json["discoverable"]) ?: false
					locked = apUtilityService.extractBoolean(json["manuallyApprovesFollowers"]) ?: false
					indexable = apUtilityService.extractBoolean(json["noindex"])?.let { !it } ?: true
					isCat = apUtilityService.extractBoolean(json["isCat"]) ?: false
					speakAsCat = apUtilityService.extractBoolean(json["speakAsCat"]) ?: false

					activated = true

					// TODO: birthday, location

					/*	TODO: this
					  createdAt =
						try {
							apUtilityService.extractString(json["published"])?.let {
								LocalDateTime.parse(it)
							} ?: timeService.now()
						} catch (e: DateTimeParseException) {
							timeService.now()
						}*/

					publicKey = sanitizerService.sanitize(extractedPublicKey, true)
				}
			}

			return userService.getById(id)
		} catch(e: Exception) {
			logger.error(e.message, e)
			return null
		}
	}
}
