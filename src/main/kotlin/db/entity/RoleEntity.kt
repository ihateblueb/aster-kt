package site.remlit.blueb.aster.db.entity

import kotlinx.datetime.LocalDateTime
import org.ktorm.entity.Entity
import site.remlit.blueb.aster.model.RoleType

interface RoleEntity : Entity<RoleEntity> {
	val id: String

	val type: RoleType

	val name: String
	val description: String?

	val createdAt: LocalDateTime
	val updatedAt: LocalDateTime?
}