package site.remlit.blueb.aster.db.entity

import kotlinx.datetime.LocalDateTime
import org.ktorm.entity.Entity
import site.remlit.blueb.aster.model.Visibility

interface NoteEntity : Entity<NoteEntity> {
	var id: String

	var apId: String
	var conversation: String?

	var user: UserEntity
	var replyingTo: NoteEntity?

	var cw: String?
	var content: String?

	var visibility: Visibility

	//var to: List<String>
	//var tags: List<String>
	//var emojis: List<String>

	var repeat: NoteEntity?

	var createdAt: LocalDateTime
	var updatedAt: LocalDateTime?

	companion object : Entity.Factory<NoteEntity>()
}