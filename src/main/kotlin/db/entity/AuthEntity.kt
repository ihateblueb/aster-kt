package site.remlit.blueb.aster.db.entity

import kotlinx.datetime.LocalDateTime
import org.ktorm.entity.Entity

interface AuthEntity : Entity<AuthEntity> {
	val id: String
	val token: String
	val user: UserEntity
	val createdAt: LocalDateTime
}