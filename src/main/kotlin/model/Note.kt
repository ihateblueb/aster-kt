package site.remlit.blueb.aster.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import site.remlit.blueb.aster.db.entity.NoteEntity

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

	val createdAt: LocalDateTime,
	val updatedAt: LocalDateTime? = null,
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
				createdAt = entity.createdAt,
				updatedAt = entity.updatedAt,
			)
		}
	}
}
