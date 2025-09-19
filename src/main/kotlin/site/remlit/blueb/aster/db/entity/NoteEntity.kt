package site.remlit.blueb.aster.db.entity

import kotlinx.datetime.LocalDateTime
import org.ktorm.entity.Entity
import site.remlit.blueb.aster.model.Visibility

interface NoteEntity : Entity<NoteEntity> {
	val id: String

	val apId: String
	val conversation: String?

	val user: UserEntity
	val replyingTo: NoteEntity?

	var cw: String?
	var content: String?

	val visibility: Visibility

	//var to: List<String>
	//var tags: List<String>
	//var emojis: List<String>

	val repeat: NoteEntity?

	val createdAt: LocalDateTime
	var updatedAt: LocalDateTime?
}