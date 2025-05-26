package me.blueb.service.ap

import io.ktor.http.Url
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.JsonObject
import me.blueb.db.entity.UserEntity
import me.blueb.db.suspendTransaction
import me.blueb.model.User
import me.blueb.model.ap.ApActor
import me.blueb.service.FormatService
import me.blueb.service.IdentifierService
import me.blueb.service.ResolverService
import me.blueb.service.SanitizerService
import me.blueb.service.TimeService
import me.blueb.service.UserService
import org.apache.commons.text.StringEscapeUtils
import org.slf4j.LoggerFactory

class ApActorService {
	private val logger = LoggerFactory.getLogger(this::class.java)

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
			return existingUser;
		}

		val resolveResponse = resolverService.resolve(apId)

		if (resolveResponse != null) {
			return register(resolveResponse)
		}

		return null
	}

	suspend fun register(json: JsonObject): UserEntity? {
		val id = identifierService.generate()

		if (json["id"] == null || json["id"].toString().isBlank()) {
			logger.debug("Actor ID is null or blank")
			return null
		}

		if (
			json["type"] == null ||
			json["type"].toString().isBlank() ||
			!listOf("Person", "Service").contains(json["type"].toString())
		) {
			logger.debug("Actor type is null, blank, or invalid")
			return null
		}

		if (json["preferredUsername"] == null || json["preferredUsername"].toString().isBlank()) {
			logger.debug("Actor preferredUsername is null or blank")
			return null
		}

		val inbox = if (json["sharedInbox"] != null)
			json["sharedInbox"].toString()
		else
			json["inbox"]?.toString()

		if (inbox == null || inbox.isBlank()) {
			logger.debug("Actor inbox is null or blank")
			return null
		}

		val summary = if (json["_misskey_summary"] != null)
			json["_misskey_summary"].toString()
		else
			json["summary"]?.toString()

		try {
			suspendTransaction {
				UserEntity.new(id) {
					apId = json["id"].toString()
					this.inbox = inbox
					outbox = json["outbox"]?.toString()

					username = sanitizerService.sanitize(json["preferredUsername"].toString(), true)
					displayName = if (json["name"] != null) sanitizerService.sanitize(json["name"].toString(), true) else null

					host = formatService.toASCII(Url(json["id"].toString()).host)

					// todo: icon, image

					bio = if (summary != null) sanitizerService.sanitize(summary) else null

					sensitive = json["sensitive"]?.toString()?.toBoolean() ?: false
					discoverable = json["discoverable"]?.toString()?.toBoolean() ?: false
					locked = json["manuallyApprovesFollowers"]?.toString()?.toBoolean() ?: false
					indexable = json["noindex"]?.toString()?.toBoolean()?.let { !it } ?: true
					isCat = json["isCat"]?.toString()?.toBoolean() ?: false
					speakAsCat = json["speakAsCat"]?.toString()?.toBoolean() ?: false

					// TODO: birthday, location

					createdAt = json["published"]?.toString()?.let { LocalDateTime.parse(it) } ?: timeService.now()
				}
			}

			return userService.getById(id)
		} catch(e: Exception) {
			logger.error(e.message, e)
			return null
		}
	}
}
