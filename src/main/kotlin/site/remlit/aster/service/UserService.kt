package site.remlit.aster.service

import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import site.remlit.aster.db.entity.UserEntity
import site.remlit.aster.db.entity.UserPrivateEntity
import site.remlit.aster.db.table.UserPrivateTable
import site.remlit.aster.db.table.UserTable
import site.remlit.aster.exception.SetupException
import site.remlit.aster.model.Configuration
import site.remlit.aster.model.Service

/**
 * Service for managing users.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
object UserService : Service {
	/**
	 * Get a user
	 *
	 * @param where Query to find user
	 *
	 * @return Found user, if any
	 * */
	@JvmStatic
	fun get(where: Op<Boolean>): UserEntity? = transaction {
		UserEntity
			.find { where }
			.singleOrNull()
	}

	/**
	 * Get a user's private information.
	 *
	 * @param where Query to find user private
	 *
	 * @return Found user private, if any
	 * */
	@JvmStatic
	fun getPrivate(where: Op<Boolean>): UserPrivateEntity? = transaction {
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
	@JvmStatic
	fun getById(id: String): UserEntity? = get(UserTable.id eq id)

	/**
	 * Get user by ActivityPub ID.
	 *
	 * @param apId ActivityPub ID of user
	 *
	 * @return Found user, if any
	 * */
	@JvmStatic
	fun getByApId(apId: String): UserEntity? = get(UserTable.apId eq apId)

	/**
	 * Get user by username.
	 *
	 * @param username Username of user
	 *
	 * @return Found user, if any
	 * */
	@JvmStatic
	fun getByUsername(username: String): UserEntity? = get(UserTable.username eq username)

	/**
	 * Get instance actor.
	 *
	 * @return Instance actor user
	 * */
	@JvmStatic
	fun getInstanceActor(): UserEntity {
		val user = getByUsername("instance.actor")
			?: throw SetupException("Instance actor can't be null")

		return user
	}

	/**
	 * Get a user's private information by ID.
	 *
	 * @param id ID of user
	 *
	 * @return Found user private, if any
	 * */
	@JvmStatic
	fun getPrivateById(id: String): UserPrivateEntity? = getPrivate(UserPrivateTable.id eq id)

	/**
	 * Get users
	 *
	 * @param where Query to find users
	 * @param take Number of users to take
	 * @param offset Offset for query
	 *
	 * @return Found users, if any
	 * */
	@JvmStatic
	fun getMany(
		where: Op<Boolean>,
		take: Int = Configuration.timeline.defaultObjects,
		offset: Long = 0
	): List<UserEntity> = transaction {
		UserEntity
			.find { where }
			.offset(offset)
			.sortedByDescending { it.createdAt }
			.take(take)
			.toList()
	}

	/**
	 * Count users
	 *
	 * @param where Query to find users
	 *
	 * @return Count of users where query applies
	 * */
	@JvmStatic
	fun count(where: Op<Boolean>): Long = transaction {
		UserEntity
			.find { where }
			.count()
	}

	/**
	 * Delete user
	 *
	 * @param where Query to find user
	 * */
	@JvmStatic
	fun delete(where: Op<Boolean>) = get(where)?.delete()
}
