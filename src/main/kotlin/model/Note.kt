package site.remlit.blueb.aster.model

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.core.eq
import site.remlit.blueb.aster.db.entity.NoteEntity
import site.remlit.blueb.aster.db.entity.NoteLikeEntity
import site.remlit.blueb.aster.db.suspendTransaction
import site.remlit.blueb.aster.db.table.NoteLikeTable

@Serializable
data class Note(
	val id: String,

	val apId: String,
	val conversation: String? = null,

	val user: User,
	val replyingTo: Note? = null,

	val cw: String? = null,
	val content: String,

	val visibility: Visibility,
	val to: List<String>? = null,
	val tags: List<String>? = null,

	val repeat: Note? = null,

	val createdAt: LocalDateTime,
	val updatedAt: LocalDateTime? = null,

	val likes: List<SmallUser> = emptyList(),
	val reactions: List<SmallUser> = emptyList(),
	val repeats: List<Note> = emptyList()
) {
	companion object {
		fun fromEntity(entity: NoteEntity): Note {
			return Note(
				id = entity.id.toString(),
				apId = entity.apId,
				conversation = entity.conversation,
				user = User.fromEntity(entity.user),
				replyingTo = null, // todo: fix Note.fromEntity(entity.replyingTo) recursion
				cw = entity.cw,
				content = entity.content,
				visibility = entity.visibility,
				to = entity.to,
				tags = entity.tags,
				repeat = null, // todo: fix Note.fromEntity(entity.repeat) recursion
				createdAt = entity.createdAt,
				updatedAt = entity.updatedAt,
				likes = runBlocking { findLikes(entity.id.toString()) }
			)
		}

		suspend fun findLikes(id: String): List<SmallUser> {
			val likes = suspendTransaction {
				NoteLikeEntity
					.find { NoteLikeTable.note eq id }
					.sortedByDescending { it.createdAt }
					.toList()
			}

			val users = mutableListOf<SmallUser>()

			likes.forEach {
				users.add(
					SmallUser.fromUser(User.fromEntity(it.user))
				)
			}

			return users.toList()
		}

		fun fromEntities(entities: List<NoteEntity>): List<Note> = entities.map { fromEntity(it) }
	}
}
