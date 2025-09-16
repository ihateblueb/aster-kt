package site.remlit.blueb.aster.db.entity

import kotlinx.datetime.LocalDateTime
import org.ktorm.entity.Entity

interface InviteEntity : Entity<InviteEntity> {
	val id: String
	val code: String

	var user: UserEntity?
	val creator: UserEntity

	val createdAt: LocalDateTime
	var usedAt: LocalDateTime?
}