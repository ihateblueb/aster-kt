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
		/**
		 * Get a policy.
		 *
		 * @param where Query to find policy
		 *
		 * @return Found policy, if any
		 * */
		suspend fun get(where: Op<Boolean>): PolicyEntity? = suspendTransaction {
			PolicyEntity
				.find { where }
				.singleOrNull()
		}

		/**
		 * Get a policy by ID.
		 *
		 * @param id ID of policy
		 *
		 * @return Found policy, if any
		 * */
		suspend fun getById(id: String): PolicyEntity? = get(PolicyTable.id eq id)

		/**
		 * Get many policies.
		 *
		 * @param where Query to find policies
		 *
		 * @return Found policies, if any
		 * */
		suspend fun getMany(where: Op<Boolean>, take: Int? = null): List<PolicyEntity> = suspendTransaction {
			PolicyEntity
				.find { where }
				.sortedByDescending { it.createdAt }
				.take(take ?: 15)
				.toList()
		}

		/**
		 * Get many policies by type.
		 *
		 * @param type Type of policy to find
		 *
		 * @return Found policies, if any
		 * */
		suspend fun getAllByType(type: PolicyType): List<PolicyEntity> = suspendTransaction {
			PolicyEntity
				.find { PolicyTable.type eq type }
				.sortedByDescending { it.createdAt }
				.toList()
		}

		/**
		 * Reduce a list of policies to their hosts
		 *
		 * @param policies List of policies to reduce
		 *
		 * @return List of hosts
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
