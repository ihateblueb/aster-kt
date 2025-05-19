package me.blueb.service

import me.blueb.db.entity.RoleEntity
import me.blueb.db.suspendTransaction
import me.blueb.db.table.RoleTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class RoleService {
	suspend fun get(where: Op<Boolean>): RoleEntity? = suspendTransaction {
		RoleEntity
			.find { where }
			.singleOrNull()
	}

	suspend fun getById(id: String): RoleEntity? = get(RoleTable.id eq id)
}
