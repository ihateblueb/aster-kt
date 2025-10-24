package site.remlit.aster.service.ap

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory
import site.remlit.aster.common.model.Note
import site.remlit.aster.db.entity.NoteEntity
import site.remlit.aster.model.Service
import site.remlit.aster.service.IdentifierService
import site.remlit.aster.service.NoteService
import site.remlit.aster.service.ResolverService
import site.remlit.aster.service.SanitizerService
import site.remlit.aster.service.TimeService
import site.remlit.aster.util.ifFails
import site.remlit.aster.util.jsonConfig

class ApNoteService : Service() {
	companion object {
		private val logger = LoggerFactory.getLogger(ApNoteService::class.java)

		/**
		 * Resolve a note by its ID
		 *
		 * @param apId ActivityPub ID of a note
		 *
		 * @return Note or null
		 * */
		suspend fun resolve(apId: String): Note? {
			val existingNote = NoteService.getByApId(apId)

			if (existingNote != null) {
				// TODO: update
				return existingNote
			}

			val resolveResponse = ResolverService.resolve(apId)

			if (resolveResponse != null)
				return register(resolveResponse)

			return null
		}

		/**
		 * Register a new note
		 *
		 * @param json JSON representation of a note
		 *
		 * @return Note or null
		 * */
		suspend fun register(json: JsonObject): Note? {
			val id = IdentifierService.generate()

			val apId = ApUtilityService.extractString(json["id"])
			if (apId.isNullOrBlank()) return null

			val type = ApUtilityService.extractString(json["type"])
			if (type.isNullOrBlank() || type != "Note") return null

			val attributedTo = ApUtilityService.extractString(json["attributedTo"])
			if (attributedTo.isNullOrBlank()) return null

			val author = ApActorService.resolve(attributedTo) ?: return null

			// todo: maximum depth, otherwise this gets messy fast
			//val inReplyTo = ApUtilityService.extractString(json["inReplyTo"])
			//val replyingTo = if (inReplyTo.isNullOrBlank()) null else resolve(inReplyTo)

			val summary = ApUtilityService.extractString(json["summary"])
			val misskeySummary = ApUtilityService.extractString(json["_misskey_summary"])

			val content = ApUtilityService.extractString(json["content"])
			val misskeyContent = ApUtilityService.extractString(json["_misskey_content"])

			val sensitive = ApUtilityService.extractBoolean(json["sensitive"]) ?: false

			val published = ApUtilityService.extractString(json["published"]).let {
				if (it != null) ifFails({ LocalDateTime.parse(it) }) {
					TimeService.now()
				} else TimeService.now()
			}

			val to = jsonConfig.decodeFromJsonElement<List<String>>(json["to"] ?: return null)
			val cc = jsonConfig.decodeFromJsonElement<List<String>>(json["cc"] ?: return null)

			val determinedVisibility =
				ApVisibilityService.determineVisibility(
					to,
					cc,
					author.followersUrl,
					ApUtilityService.extractString(json["visibility"])
				)

			val finalSummary = misskeySummary ?: summary
			val finalContent = misskeyContent ?: content

			try {
				transaction {
					NoteEntity.new(id) {
						this.apId = apId
						this.user = author
						this.cw = if (finalSummary != null) SanitizerService.sanitize(finalSummary) else null
						// todo: note nullability
						this.content = if (finalContent != null) SanitizerService.sanitize(finalContent) else ""
						this.visibility = determinedVisibility
						this.to = emptyList()

						// todo: to
						// todo: tags
						// todo: emojis
						// todo: repeat

						this.createdAt = published
					}
				}

				return NoteService.getById(id)
			} catch (e: Exception) {
				logger.error(e.message, e)
				return null
			}
		}
	}
}