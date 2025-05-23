package me.blueb.service

import me.blueb.db.entity.PolicyEntity
import me.blueb.db.entity.RoleEntity
import me.blueb.db.suspendTransaction
import me.blueb.db.table.PolicyTable
import me.blueb.db.table.RoleTable
import me.blueb.model.PolicyType
import me.blueb.model.RoleType
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class RoleService {
	private val userService = UserService()

	suspend fun get(where: Op<Boolean>): RoleEntity? = suspendTransaction {
		RoleEntity
			.find { where }
			.singleOrNull()
	}

	suspend fun getMany(where: Op<Boolean>): List<RoleEntity> = suspendTransaction {
		RoleEntity
			.find { where }
			.toList()
	}

	suspend fun getAll(): List<RoleEntity> = suspendTransaction {
		RoleEntity
			.all()
			.toList()
	}

	suspend fun getById(id: String): RoleEntity? = get(RoleTable.id eq id)

	suspend fun userHasRoleOfType(userId: String, type: RoleType): Boolean {
		val user = userService.getById(userId)
		val rolesOfType = this.getMany(RoleTable.type eq type)

		if (user != null && rolesOfType.any { it.id.toString() in user.roles })
			return true

		return false
	}

	// todo: userHasRoleType with Admin,Mod
}
