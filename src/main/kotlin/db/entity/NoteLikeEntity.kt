package site.remlit.blueb.aster.db.entity

import kotlinx.datetime.LocalDateTime
import org.ktorm.entity.Entity

interface NoteLikeEntity : Entity<NoteLikeEntity> {
	val id: String

	val user: UserEntity
	val note: NoteEntity

	val createdAt: LocalDateTime
}