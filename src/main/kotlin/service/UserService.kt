package site.remlit.blueb.service

import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import site.remlit.blueb.db.entity.UserEntity
import site.remlit.blueb.db.entity.UserPrivateEntity
import site.remlit.blueb.db.suspendTransaction
import site.remlit.blueb.db.table.UserPrivateTable
import site.remlit.blueb.db.table.UserTable

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
