package site.remlit.blueb.aster.util.model

import site.remlit.blueb.aster.common.model.Note
import site.remlit.blueb.aster.common.model.Notification
import site.remlit.blueb.aster.common.model.Relationship
import site.remlit.blueb.aster.common.model.User
import site.remlit.blueb.aster.db.entity.NotificationEntity

fun Notification.Companion.fromEntity(entity: NotificationEntity): Notification = Notification(
	id = entity.id.toString(),
	type = entity.type,
	to = User.fromEntity(entity.to),
	from = User.fromEntity(entity.from),
	note = Note.fromEntity(entity.note),
	relationship = Relationship.fromEntity(entity.relationship),
	createdAt = entity.createdAt,
)

fun Notification.Companion.fromEntities(entities: List<NotificationEntity>): List<Notification> =
	entities.map { fromEntity(it) }