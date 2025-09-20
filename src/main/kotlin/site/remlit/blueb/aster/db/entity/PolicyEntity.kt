package site.remlit.blueb.aster.db.entity

import kotlinx.datetime.LocalDateTime
import org.ktorm.entity.Entity
import site.remlit.blueb.aster.model.PolicyType

interface PolicyEntity : Entity<PolicyEntity> {
	var id: String
	var type: PolicyType

	var host: String?
	var content: String?

	var createdAt: LocalDateTime
	var updatedAt: LocalDateTime?

	companion object : Entity.Factory<PolicyEntity>()
}