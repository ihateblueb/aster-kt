package me.blueb.service

import me.blueb.db.entity.PolicyEntity
import me.blueb.db.suspendTransaction
import me.blueb.db.table.PolicyTable
import me.blueb.model.PolicyType
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PolicyService {
	suspend fun get(where: Op<Boolean>): PolicyEntity? = suspendTransaction {
		PolicyEntity
			.find { where }
			.singleOrNull()
	}

	suspend fun getById(id: String): PolicyEntity? = get(PolicyTable.id eq id)

	suspend fun getMany(where: Op<Boolean>, take: Int? = null): List<PolicyEntity> = suspendTransaction {
		PolicyEntity
			.find { where }
			.take(take ?: 15)
			.sortedBy { it.createdAt }
			.toList()
	}

	suspend fun getAllByType(type: PolicyType): List<PolicyEntity> = suspendTransaction {
		PolicyEntity
			.find { PolicyTable.type eq type }
			.sortedBy { it.createdAt }
			.toList()
	}

	/**
	 * Reduce a [List] of [PolicyEntity] to [PolicyEntity.host]
	 *
	 * @return [List] of [PolicyEntity.host] as [String]
	 * */
	fun reducePoliciesInListToHost(policies: List<PolicyEntity>): List<String> {
		val reducedHosts: MutableList<String> = mutableListOf()

		for (policy in policies) {
			reducedHosts.add(policy.host)
		}

		return reducedHosts
	}
}
