package site.remlit.blueb.aster.service

import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import site.remlit.blueb.aster.db.entity.NoteEntity
import site.remlit.blueb.aster.db.entity.NoteLikeEntity
import site.remlit.blueb.aster.db.entity.UserEntity
import site.remlit.blueb.aster.db.suspendTransaction
import site.remlit.blueb.aster.db.table.NoteLikeTable
import site.remlit.blueb.aster.db.table.NoteTable
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.event.note.NoteCreateEvent
import site.remlit.blueb.aster.event.note.NoteDeleteEvent
import site.remlit.blueb.aster.event.note.NoteLikeEvent
import site.remlit.blueb.aster.event.note.NoteUnlikeEvent
import site.remlit.blueb.aster.exception.InsertFailureException
import site.remlit.blueb.aster.exception.TargetNotFoundException
import site.remlit.blueb.aster.model.Note
import site.remlit.blueb.aster.model.Service
import site.remlit.blueb.aster.model.User
import site.remlit.blueb.aster.model.Visibility
import site.remlit.blueb.aster.service.ap.ApIdService

class NoteService : Service() {
	companion object {
		suspend fun get(where: Op<Boolean>): Note? = suspendTransaction {
			val note = NoteEntity
				.find { where }
				.singleOrNull()
				?.load(NoteEntity::user)

			if (note != null)
				Note.fromEntity(note)
			else null
		}

		suspend fun getById(id: String): Note? = get(NoteTable.id eq id)
		suspend fun getByApId(apId: String): Note? = get(NoteTable.apId eq apId)

		suspend fun getMany(where: Op<Boolean>, take: Int? = null): List<Note> = suspendTransaction {
			val notes = NoteEntity
				.find { where }
				.sortedByDescending { it.createdAt }
				.take(take ?: 15)
				.toList()

			if (!notes.isEmpty())
				Note.fromEntities(notes)
			else listOf()
		}

		suspend fun count(where: Op<Boolean>): Long = suspendTransaction {
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
		suspend fun create(
			id: String = IdentifierService.generate(),
			user: UserEntity,
			cw: String?,
			content: String,
			visibility: Visibility,
			to: List<String> = listOf(),
			tags: List<String> = listOf()
		): Note {
			suspendTransaction {
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
		suspend fun like(
			user: User,
			noteId: String,
		) {
			val note = getById(noteId) ?: throw TargetNotFoundException("Note not found")

			if (!VisibilityService.canISee(note.visibility, note.user.id, note.to, user.id))
				throw TargetNotFoundException("Note not found")

			val existing = suspendTransaction {
				NoteLikeEntity
					.find {
						NoteLikeTable.note eq note.id and
								(NoteLikeTable.user eq user.id)
					}
					.singleOrNull()
			}

			if (existing != null) {
				suspendTransaction { existing.delete() }
				NoteUnlikeEvent(note, user).call()
				return
			}

			suspendTransaction {
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
		suspend fun delete(where: Op<Boolean>) = suspendTransaction {
			val entity = NoteEntity
				.find { where }
				.singleOrNull()
			if (entity == null) return@suspendTransaction

			NoteDeleteEvent(Note.fromEntity(entity)).call()
			entity.delete()
		}

		/**
		 * Delete a note by ID.
		 *
		 * @param id ID of note
		 * */
		suspend fun deleteById(id: String) = delete(NoteTable.id eq id)

		/**
		 * Delete a note by ActivityPub ID.
		 *
		 * @param apId ActivityPub ID of note
		 * */
		suspend fun deleteByApId(apId: String) = delete(NoteTable.id eq apId)
	}
}
