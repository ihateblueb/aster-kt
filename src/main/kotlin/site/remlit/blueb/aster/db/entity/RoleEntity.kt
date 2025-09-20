package site.remlit.blueb.aster.db.entity

import kotlinx.datetime.LocalDateTime
import org.ktorm.entity.Entity
import site.remlit.blueb.aster.model.RoleType

interface RoleEntity : Entity<RoleEntity> {
	var id: String

	var type: RoleType

	var name: String
	var description: String?

	var createdAt: LocalDateTime
	var updatedAt: LocalDateTime?

	companion object : Entity.Factory<RoleEntity>()
}