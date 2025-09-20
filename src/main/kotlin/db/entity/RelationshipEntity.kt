package site.remlit.blueb.aster.db.entity

import kotlinx.datetime.LocalDateTime
import org.ktorm.entity.Entity
import site.remlit.blueb.aster.model.RelationshipType

interface RelationshipEntity : Entity<RelationshipEntity> {
	val id: String
	var type: RelationshipType

	val to: UserEntity
	val from: UserEntity

	val activityId: String

	val createdAt: LocalDateTime
	val updatedAt: LocalDateTime?
}