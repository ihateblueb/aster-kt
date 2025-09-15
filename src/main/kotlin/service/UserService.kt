package site.remlit.blueb.aster.service

import org.ktorm.dsl.*
import org.ktorm.schema.ColumnDeclaring
import site.remlit.blueb.aster.db.Database.database
import site.remlit.blueb.aster.db.entity.UserEntity
import site.remlit.blueb.aster.db.entity.UserPrivateEntity
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
		suspend fun get(where: () -> ColumnDeclaring<Boolean>): UserEntity? =
			database
				.from(UserTable)
				.select()
				.where(where)
				.limit(1)
				.map { row -> UserTable.createEntity(row) }
				.firstOrNull()
		
		/**
		 * Get users.
		 *
		 * @param where Query to find users
		 *
		 * @return Found users
		 * */
		fun getMany(where: () -> ColumnDeclaring<Boolean>): List<UserEntity> =
			database
				.from(UserTable)
				.select()
				.where(where)
				.map { row -> UserTable.createEntity(row) }

		/**
		 * Get a user's private information.
		 *
		 * @param where Query to find user private
		 *
		 * @return Found user private, if any
		 * */
		suspend fun getPrivate(where: () -> ColumnDeclaring<Boolean>): UserPrivateEntity? =
			database
				.from(UserPrivateTable)
				.select()
				.where(where)
				.limit(1)
				.map { row -> UserPrivateTable.createEntity(row) }
				.firstOrNull()

		/**
		 * Get user by ID.
		 *
		 * @param id ID of user
		 *
		 * @return Found user, if any
		 * */
		suspend fun getById(id: String): UserEntity? = get { UserTable.id eq id }

		/**
		 * Get user by ActivityPub ID.
		 *
		 * @param apId ActivityPub ID of user
		 *
		 * @return Found user, if any
		 * */
		suspend fun getByApId(apId: String): UserEntity? = get { UserTable.apId eq apId }

		/**
		 * Get user by username.
		 *
		 * @param username Username of user
		 *
		 * @return Found user, if any
		 * */
		suspend fun getByUsername(username: String): UserEntity? = get { UserTable.username eq username }

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
		suspend fun getPrivateById(id: String): UserPrivateEntity? = getPrivate { UserPrivateTable.id eq id }

		/**
		 * Count users
		 *
		 * @param where Query to find users
		 *
		 * @return Count of users where query applies
		 * */
		suspend fun count(where: () -> ColumnDeclaring<Boolean>): Int =
			database
				.from(UserTable)
				.select()
				.where(where)
				.totalRecordsInAllPages


		/**
		 * Delete user
		 *
		 * @param where Query to find user
		 * */
		suspend fun delete(where: () -> ColumnDeclaring<Boolean>) {
			get(where)?.delete()
		}
	}
}
