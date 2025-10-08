package site.remlit.blueb.aster.service

import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.eq
import site.remlit.blueb.aster.db.entity.UserEntity
import site.remlit.blueb.aster.db.entity.UserPrivateEntity
import site.remlit.blueb.aster.db.suspendTransaction
import site.remlit.blueb.aster.db.table.UserPrivateTable
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.model.Service

class UserService() : Service() {
	companion object {
		/**
		 * Get a user.
		 *
		 * @param where Query to find user
		 *
		 * @return Found user, if any
		 * */
		suspend fun get(where: Op<Boolean>): UserEntity? = suspendTransaction {
			UserEntity
				.find { where }
				.singleOrNull()
		}

		/**
		 * Get a user.
		 *
		 * @param where Query to find user
		 *
		 * @return Found user, if any
		 * */
		suspend fun getMany(where: Op<Boolean>): List<UserEntity> = suspendTransaction {
			UserEntity
				.find { where }
				.toList()
		}

		/**
		 * Get a user's private information.
		 *
		 * @param where Query to find user private
		 *
		 * @return Found user private, if any
		 * */
		suspend fun getPrivate(where: Op<Boolean>): UserPrivateEntity? = suspendTransaction {
			UserPrivateEntity
				.find { where }
				.singleOrNull()
		}

		/**
		 * Get user by ID.
		 *
		 * @param id ID of user
		 *
		 * @return Found user, if any
		 * */
		suspend fun getById(id: String): UserEntity? = get(UserTable.id eq id)

		/**
		 * Get user by ActivityPub ID.
		 *
		 * @param apId ActivityPub ID of user
		 *
		 * @return Found user, if any
		 * */
		suspend fun getByApId(apId: String): UserEntity? = get(UserTable.apId eq apId)

		/**
		 * Get user by username.
		 *
		 * @param username Username of user
		 *
		 * @return Found user, if any
		 * */
		suspend fun getByUsername(username: String): UserEntity? = get(UserTable.username eq username)

		/**
		 * Get instance actor.
		 *
		 * @return Instance actor user
		 * */
		suspend fun getInstanceActor(): UserEntity {
			val user = getByUsername("instance.actor") ?: throw RuntimeException("Instance actor can't be null")

			return user
		}

		/**
		 * Get a user's private information by ID.
		 *
		 * @param id ID of user
		 *
		 * @return Found user private, if any
		 * */
		suspend fun getPrivateById(id: String): UserPrivateEntity? = getPrivate(UserPrivateTable.id eq id)

		/**
		 * Count users
		 *
		 * @param where Query to find users
		 *
		 * @return Count of users where query applies
		 * */
		suspend fun count(where: Op<Boolean>): Long = suspendTransaction {
			UserEntity
				.find { where }
				.count()
		}

		/**
		 * Delete user
		 *
		 * @param where Query to find user
		 * */
		suspend fun delete(where: Op<Boolean>) = get(where)?.delete()
	}
}
