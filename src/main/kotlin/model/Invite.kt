package site.remlit.blueb.aster.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import site.remlit.blueb.aster.db.entity.InviteEntity

@Serializable
data class Invite(
	val id: String,

	val code: String,

	val user: User? = null,
	val creator: User,

	val createdAt: LocalDateTime,
	val usedAt: LocalDateTime? = null,
) {
	companion object {
		fun fromEntity(entity: InviteEntity): Invite = Invite(
			id = entity.id.toString(),
			code = entity.code,
			user = if (entity.user != null) User.fromEntity(entity.user!!) else null,
			creator = User.fromEntity(entity.creator),
			createdAt = entity.createdAt,
			usedAt = entity.usedAt
		)
	}
}
