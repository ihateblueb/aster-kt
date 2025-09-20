package site.remlit.blueb.aster.db.entity

import kotlinx.datetime.LocalDateTime
import site.remlit.blueb.aster.model.PolicyType

interface PolicyEntity {
	val id: String
	val type: PolicyType

	val host: String?
	val content: String?

	val createdAt: LocalDateTime
	val updatedAt: LocalDateTime?
}