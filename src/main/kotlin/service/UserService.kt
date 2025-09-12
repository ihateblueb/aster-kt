package site.remlit.blueb.aster.service

import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import site.remlit.blueb.aster.db.entity.UserEntity
import site.remlit.blueb.aster.db.entity.UserPrivateEntity
import site.remlit.blueb.aster.db.suspendTransaction
import site.remlit.blueb.aster.db.table.UserPrivateTable
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.model.Service

class UserService() : Service() {
	companion object {
		suspend fun get(where: Op<Boolean>): UserEntity? = suspendTransaction {
			UserEntity
				.find { where }
				.singleOrNull()
		}

		suspend fun getMany(where: Op<Boolean>): List<UserEntity> = suspendTransaction {
			UserEntity
				.find { where }
				.toList()
		}

		suspend fun getPrivate(where: Op<Boolean>): UserPrivateEntity? = suspendTransaction {
			UserPrivateEntity
				.find { where }
				.singleOrNull()
		}

		suspend fun getById(id: String): UserEntity? = get(UserTable.id eq id)
		suspend fun getByApId(apId: String): UserEntity? = get(UserTable.apId eq apId)
		suspend fun getByUsername(username: String): UserEntity? = get(UserTable.username eq username)

		suspend fun getInstanceActor(): UserEntity {
			val user = getByUsername("instance.actor") ?: throw RuntimeException("Instance actor can't be null")

			return user
		}

		suspend fun getPrivateById(id: String): UserPrivateEntity? = getPrivate(UserPrivateTable.id eq id)

		suspend fun count(where: Op<Boolean>): Long = suspendTransaction {
			UserEntity
				.find { where }
				.count()
		}

		suspend fun delete(where: Op<Boolean>) = get(where)?.delete()
	}
}
