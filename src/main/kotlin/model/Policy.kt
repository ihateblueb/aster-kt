package site.remlit.blueb.aster.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import site.remlit.blueb.aster.db.entity.PolicyEntity

@Serializable
data class Policy(
	val id: String,

	val type: PolicyType,

	val host: String,
	val content: String? = null,

	val createdAt: LocalDateTime,
	val updatedAt: LocalDateTime? = null
) {
	companion object {
		fun fromEntity(entity: PolicyEntity): Policy = Policy(
			id = entity.id.toString(),
			type = entity.type,
			host = entity.host,
			content = entity.content,
			createdAt = entity.createdAt,
			updatedAt = entity.updatedAt
		)

		fun fromEntities(entities: List<PolicyEntity>): List<Policy> = entities.map { fromEntity(it) }
	}
}
