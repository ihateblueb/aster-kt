package site.remlit.blueb.aster.service

import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import site.remlit.blueb.aster.common.model.type.PolicyType
import site.remlit.blueb.aster.db.entity.PolicyEntity
import site.remlit.blueb.aster.db.table.PolicyTable
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.Service

/**
 * Service for managing federation policies.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
class PolicyService : Service() {
	companion object {
		/**
		 * Get a policy.
		 *
		 * @param where Query to find policy
		 *
		 * @return Found policy, if any
		 * */
		fun get(where: Op<Boolean>): PolicyEntity? = transaction {
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
		fun getById(id: String): PolicyEntity? = get(PolicyTable.id eq id)

		/**
		 * Get many policies.
		 *
		 * @param where Query to find policies
		 *
		 * @return Found policies, if any
		 * */
		fun getMany(where: Op<Boolean>, take: Int? = null): List<PolicyEntity> = transaction {
			PolicyEntity
				.find { where }
				.sortedByDescending { it.createdAt }
				.take(take ?: Configuration.timeline.defaultObjects)
				.toList()
		}

		/**
		 * Get many policies by type.
		 *
		 * @param type Type of policy to find
		 *
		 * @return Found policies, if any
		 * */
		fun getAllByType(type: PolicyType): List<PolicyEntity> = transaction {
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
