package site.remlit.blueb.aster.db.entity

import kotlinx.datetime.LocalDateTime
import org.ktorm.entity.Entity
import site.remlit.blueb.aster.model.PolicyType

interface PolicyEntity : Entity<PolicyEntity> {
	val id: String
	val type: PolicyType

	val host: String?
	val content: String?

	val createdAt: LocalDateTime
	val updatedAt: LocalDateTime?
}