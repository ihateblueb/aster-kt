package me.blueb.service

import me.blueb.db.entity.PolicyEntity
import me.blueb.db.suspendTransaction
import me.blueb.db.table.PolicyTable
import me.blueb.model.PolicyType

class PolicyService {
	suspend fun getAllByType(type: PolicyType): List<PolicyEntity> = suspendTransaction {
		PolicyEntity
			.find { PolicyTable.type eq type }
			.toList()
	}
}
