package site.remlit.blueb.aster.service

import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.load
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import site.remlit.blueb.aster.common.model.Note
import site.remlit.blueb.aster.common.model.User
import site.remlit.blueb.aster.common.model.Visibility
import site.remlit.blueb.aster.db.entity.NoteEntity
import site.remlit.blueb.aster.db.entity.NoteLikeEntity
import site.remlit.blueb.aster.db.entity.UserEntity
import site.remlit.blueb.aster.db.table.NoteLikeTable
import site.remlit.blueb.aster.db.table.NoteTable
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.event.note.NoteCreateEvent
import site.remlit.blueb.aster.event.note.NoteDeleteEvent
import site.remlit.blueb.aster.event.note.NoteLikeEvent
import site.remlit.blueb.aster.event.note.NoteUnlikeEvent
import site.remlit.blueb.aster.exception.InsertFailureException
import site.remlit.blueb.aster.exception.TargetNotFoundException
import site.remlit.blueb.aster.model.Service
import site.remlit.blueb.aster.service.ap.ApIdService
import site.remlit.blueb.aster.util.model.fromEntities
import site.remlit.blueb.aster.util.model.fromEntity

/**
 * Service for managing notes.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
class NoteService : Service() {
	companion object {
		fun get(where: Op<Boolean>): Note? = transaction {
			val note = NoteEntity
				.find { where }
				.singleOrNull()
				?.load(NoteEntity::user)

			if (note != null)
				Note.fromEntity(note)
			else null
		}

		fun getById(id: String): Note? = get(NoteTable.id eq id)
		fun getByApId(apId: String): Note? = get(NoteTable.apId eq apId)

		fun getMany(where: Op<Boolean>, take: Int? = null): List<Note> = transaction {
			val notes = NoteEntity
				.find { where }
				.sortedByDescending { it.createdAt }
				.take(take ?: 15)
				.toList()

			if (!notes.isEmpty())
				Note.fromEntities(notes)
			else listOf()
		}

		fun count(where: Op<Boolean>): Long = transaction {
			NoteTable
				.leftJoin(UserTable)
				.select(where)
				.count()
		}

		/**
		 * Create a note
		 *
		 * @param id ID of the note
		 * @param user User authoring the post
		 * @param cw Content warning of the note
		 * @param content Content of the note
		 * @param visibility Visibility of the note
		 * @param to List of users mentioned
		 * @param tags List of extracted hashtags
		 *
		 * @return Created note
		 * */
		fun create(
			id: String = IdentifierService.generate(),
			user: UserEntity,
			cw: String?,
			content: String,
			visibility: Visibility,
			to: List<String> = listOf(),
			tags: List<String> = listOf()
		): Note {
			transaction {
				NoteEntity.new(id) {
					apId = ApIdService.renderNoteApId(id)
					this.user = user
					this.cw = if (cw != null) SanitizerService.sanitize(cw, true) else null
					this.content = SanitizerService.sanitize(content, true)
					this.visibility = visibility
					this.to = to
					this.tags = tags
				}
			}

			val note = getById(id) ?: throw InsertFailureException("Failed to create note")
			NoteCreateEvent(note).call()

			return note
		}

		/**
		 * Like a note as a user, or removes a like if it's already there.
		 *
		 * @param user User liking the note
		 * @param noteId ID of the target note
		 *
		 * @since 2025.9.1.1-SNAPSHOT
		 * */
		fun like(
			user: User,
			noteId: String,
		) {
			val note = getById(noteId)
				?: throw TargetNotFoundException("Note not found")

			if (!VisibilityService.canISee(note.visibility, note.user.id, note.to, user.id))
				throw TargetNotFoundException("Note not found")

			val existing = transaction {
				NoteLikeEntity
					.find {
						NoteLikeTable.note eq note.id and
								(NoteLikeTable.user eq user.id)
					}
					.singleOrNull()
			}

			if (existing != null) {
				transaction { existing.delete() }
				NoteUnlikeEvent(note, user).call()
				return
			}

			transaction {
				NoteLikeEntity.new(IdentifierService.generate()) {
					this.user = UserEntity[user.id]
					this.note = NoteEntity[note.id]
				}
			}

			NoteLikeEvent(note, user).call()
		}

		/**
		 * Delete a note.
		 *
		 * @param where Query to find note
		 * */
		fun delete(where: Op<Boolean>) = transaction {
			val entity = NoteEntity
				.find { where }
				.singleOrNull()
			if (entity == null) return@transaction

			NoteDeleteEvent(Note.fromEntity(entity)).call()
			entity.delete()
		}

		/**
		 * Delete a note by ID.
		 *
		 * @param id ID of note
		 * */
		fun deleteById(id: String) = delete(NoteTable.id eq id)

		/**
		 * Delete a note by ActivityPub ID.
		 *
		 * @param apId ActivityPub ID of note
		 * */
		fun deleteByApId(apId: String) = delete(NoteTable.id eq apId)
	}
}
