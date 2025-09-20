package site.remlit.blueb.aster.db.entity

import kotlinx.datetime.LocalDateTime
import org.ktorm.entity.Entity

interface NoteLikeEntity : Entity<NoteLikeEntity> {
	var id: String

	var user: UserEntity
	var note: NoteEntity

	var createdAt: LocalDateTime

	companion object : Entity.Factory<NoteLikeEntity>()
}