package site.remlit.blueb.aster.db.entity

import kotlinx.datetime.LocalDateTime
import org.ktorm.entity.Entity

interface InviteEntity : Entity<InviteEntity> {
	var id: String
	var code: String

	var user: UserEntity?
	var creator: UserEntity

	var createdAt: LocalDateTime
	var usedAt: LocalDateTime?

	companion object : Entity.Factory<InviteEntity>()
}