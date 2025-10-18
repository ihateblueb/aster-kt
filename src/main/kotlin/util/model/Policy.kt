package site.remlit.blueb.aster.util.model

import site.remlit.blueb.aster.common.model.Policy
import site.remlit.blueb.aster.db.entity.PolicyEntity


fun Policy.Companion.fromEntity(entity: PolicyEntity): Policy = Policy(
	id = entity.id.toString(),
	type = entity.type,
	host = entity.host,
	content = entity.content,
	createdAt = entity.createdAt,
	updatedAt = entity.updatedAt
)

fun Policy.Companion.fromEntities(entities: List<PolicyEntity>): List<Policy> = entities.map { fromEntity(it) }