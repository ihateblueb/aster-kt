package site.remlit.aster.service.ap

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory
import site.remlit.aster.common.model.Note
import site.remlit.aster.common.model.User
import site.remlit.aster.common.model.Visibility
import site.remlit.aster.common.model.generated.PartialNote
import site.remlit.aster.db.entity.NoteEntity
import site.remlit.aster.model.Service
import site.remlit.aster.service.IdentifierService
import site.remlit.aster.service.NoteService
import site.remlit.aster.service.ResolverService
import site.remlit.aster.service.TimeService
import site.remlit.aster.service.UserService
import site.remlit.aster.util.ifFails
import site.remlit.aster.util.jsonConfig
import site.remlit.aster.util.model.fromEntity

/**
 * Service to handle ActivityPub notes.
 *
 * @since 2025.10.5.0-SNAPSHOT
 * */
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
		suspend fun resolve(apId: String, refetch: Boolean = false): Note? {
			val existingNote = NoteService.getByApId(apId)

			if ((existingNote != null) && !refetch) {
				return existingNote
			}

			val resolveResponse = ResolverService.resolveSigned(apId)

			if (resolveResponse != null && existingNote == null)
				return register(toNote(resolveResponse) ?: return null)

			if (resolveResponse != null && existingNote != null)
				return update(toNote(resolveResponse, existingNote) ?: return null)

			return null
		}

		// partials used here since a regular note has the expectation of being real,
		// has calculated fields so creating a regular note where would waste a query and potentially error
		suspend fun toNote(json: JsonObject, existing: Note? = null): PartialNote? {
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

			return PartialNote(
				id = existing?.id ?: IdentifierService.generate(),
				apId = existing?.apId ?: apId,
				user = User.fromEntity(author),
				conversation = null,

				cw = finalSummary,
				content = finalContent,
				visibility = determinedVisibility,
				tags = null,
				to = null,

				replyingTo = existing?.replyingTo,
				repeat = existing?.repeat,

				likes = existing?.likes,
				reactions = existing?.reactions,
				repeats = existing?.repeats,

				createdAt = published,
				updatedAt = if (existing != null) TimeService.now() else null,
			)
		}

		/**
		 * Update a note
		 *
		 * @param note Converted note to partial note
		 *
		 * @return Note or null
		 * */
		fun update(note: PartialNote): Note? {
			try {
				transaction {
					NoteEntity.findByIdAndUpdate(note.id!!) {
						it.apId = note.apId!!
						it.user = UserService.getById(note.user?.id!!)!!
						it.cw = note.cw
						// todo: note nullability
						it.content = note.content ?: ""
						it.visibility = note.visibility ?: Visibility.Direct

						// todo: to
						it.to = note.to ?: emptyList()
						// todo: tags
						// todo: emojis
						// todo: repeat

						it.createdAt = note.createdAt!!
					}
				}

				return NoteService.getById(note.id!!)
			} catch (e: Exception) {
				logger.error(e.message, e)
				return null
			}
		}

		/**
		 * Register a new note
		 *
		 * @param note Converted note to partial note
		 *
		 * @return Note or null
		 * */
		fun register(note: PartialNote): Note? {
			try {
				transaction {
					NoteEntity.new(note.id!!) {
						this.apId = note.apId!!
						this.user = UserService.getById(note.user?.id!!)!!
						this.cw = note.cw
						// todo: note nullability
						this.content = note.content ?: ""
						this.visibility = note.visibility ?: Visibility.Direct

						// todo: to
						this.to = note.to ?: emptyList()
						// todo: tags
						// todo: emojis
						// todo: repeat

						this.createdAt = note.createdAt!!
					}
				}

				return NoteService.getById(note.id!!)
			} catch (e: Exception) {
				logger.error(e.message, e)
				return null
			}
		}
	}
}