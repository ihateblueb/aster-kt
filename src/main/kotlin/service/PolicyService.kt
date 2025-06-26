package site.remlit.blueb.aster.service

import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import site.remlit.blueb.aster.db.entity.PolicyEntity
import site.remlit.blueb.aster.db.suspendTransaction
import site.remlit.blueb.aster.db.table.PolicyTable
import site.remlit.blueb.aster.model.PolicyType
import site.remlit.blueb.aster.model.Service

class PolicyService : Service() {
	companion object {
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
}
