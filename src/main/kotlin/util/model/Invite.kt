package site.remlit.blueb.aster.util.model

import site.remlit.blueb.aster.common.model.Invite
import site.remlit.blueb.aster.common.model.User
import site.remlit.blueb.aster.db.entity.InviteEntity


fun Invite.Companion.fromEntity(entity: InviteEntity): Invite = Invite(
	id = entity.id.toString(),
	code = entity.code,
	user = if (entity.user != null) User.fromEntity(entity.user!!) else null,
	creator = User.fromEntity(entity.creator),
	createdAt = entity.createdAt,
	usedAt = entity.usedAt
)

fun Invite.Companion.fromEntities(entities: List<InviteEntity>): List<Invite> = entities.map { fromEntity(it) }