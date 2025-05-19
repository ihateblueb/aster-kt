package me.blueb.service

import me.blueb.db.entity.RoleEntity
import me.blueb.db.suspendTransaction
import me.blueb.db.table.RoleTable
import me.blueb.db.table.UserTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class RoleService {
	private val userService = UserService()

	suspend fun get(where: Op<Boolean>): RoleEntity? = suspendTransaction {
		RoleEntity
			.find { where }
			.singleOrNull()
	}

	suspend fun getById(id: String): RoleEntity? = get(RoleTable.id eq id)

	suspend fun userHasRole(userId: String, roleId: String): Boolean {
		val user = userService.getById(userId)

		if (user != null && user.roles.contains(roleId))
			return true

		return false
	}

	// todo: userHasRoleType with Admin,Mod
}
