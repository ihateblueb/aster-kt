package site.remlit.blueb.aster.db.entity

import kotlinx.datetime.LocalDateTime
import org.ktorm.entity.Entity
import site.remlit.blueb.aster.model.NotificationType

interface NotificationEntity : Entity<NotificationEntity> {
	var id: String
	var type: NotificationType

	var to: UserEntity
	var from: UserEntity?

	var note: NoteEntity?
	var relationship: RelationshipEntity?

	var createdAt: LocalDateTime

	companion object : Entity.Factory<NotificationEntity>()
}