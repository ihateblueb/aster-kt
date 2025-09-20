package site.remlit.blueb.aster.db.entity

import kotlinx.datetime.LocalDateTime
import org.ktorm.entity.Entity

interface AuthEntity : Entity<AuthEntity> {
	var id: String

	var token: String
	var user: UserEntity

	var createdAt: LocalDateTime

	companion object : Entity.Factory<AuthEntity>()
}