package me.blueb.service

import me.blueb.db.entity.UserEntity
import me.blueb.db.entity.UserPrivateEntity
import me.blueb.db.suspendTransaction
import me.blueb.db.table.UserPrivateTable
import me.blueb.db.table.UserTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserService() {
	suspend fun get(where: Op<Boolean>): UserEntity? = suspendTransaction {
		UserEntity
			.find { where }
			.singleOrNull()
	}

	suspend fun getPrivate(where: Op<Boolean>): UserPrivateEntity? = suspendTransaction {
		UserPrivateEntity
			.find { where }
			.singleOrNull()
	}

    suspend fun getById(id: String): UserEntity? = get(UserTable.id eq id)
    suspend fun getByApId(apId: String): UserEntity? = get(UserTable.apId eq apId)
	suspend fun getByUsername(username: String): UserEntity? = get(UserTable.username eq username)

	suspend fun getPrivateById(id: String): UserPrivateEntity? = getPrivate(UserPrivateTable.id eq id)

	suspend fun count(where: Op<Boolean>): Long = suspendTransaction {
		UserEntity
			.find { where }
			.count()
	}

	suspend fun delete(where: Op<Boolean>) = get(where)?.delete()
}
