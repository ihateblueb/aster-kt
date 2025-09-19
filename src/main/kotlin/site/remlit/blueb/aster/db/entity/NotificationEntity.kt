package site.remlit.blueb.aster.db.entity

import kotlinx.datetime.LocalDateTime
import org.ktorm.entity.Entity
import site.remlit.blueb.aster.model.NotificationType

interface NotificationEntity : Entity<NotificationEntity> {
	val id: String
	val type: NotificationType

	val to: UserEntity
	val from: UserEntity?

	val note: NoteEntity?
	val relationship: RelationshipEntity?

	val createdAt: LocalDateTime
}