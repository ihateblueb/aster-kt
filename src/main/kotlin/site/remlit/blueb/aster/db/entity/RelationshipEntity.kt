package site.remlit.blueb.aster.db.entity

import kotlinx.datetime.LocalDateTime
import org.ktorm.entity.Entity
import site.remlit.blueb.aster.model.RelationshipType

interface RelationshipEntity : Entity<RelationshipEntity> {
	var id: String
	var type: RelationshipType

	var to: UserEntity
	var from: UserEntity

	var activityId: String

	var createdAt: LocalDateTime
	var updatedAt: LocalDateTime?

	companion object : Entity.Factory<RelationshipEntity>()
}