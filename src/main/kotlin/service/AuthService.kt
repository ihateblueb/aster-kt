package site.remlit.blueb.aster.service

import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.eq
import site.remlit.blueb.aster.db.entity.AuthEntity
import site.remlit.blueb.aster.db.suspendTransaction
import site.remlit.blueb.aster.db.table.AuthTable
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.model.Service

/**
 * Service for managing user authentication.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
class AuthService : Service() {
	companion object {
		/**
		 * Get auth entity.
		 *
		 * @param where Query to find auth entity
		 *
		 * @return Auth entity, if it exists
		 * */
		suspend fun get(where: Op<Boolean>): AuthEntity? = suspendTransaction {
			AuthEntity
				.find { where }
				.singleOrNull()
		}

		/**
		 * Get auth entity by token.
		 *
		 * @param token Token to use to find an auth entity
		 *
		 * @return Auth entity, if it exists
		 * */
		suspend fun getByToken(token: String): AuthEntity? = get(AuthTable.token eq token)

		/**
		 * Creates a new auth token for a user
		 *
		 * @param user ID of a user
		 *
		 * @return Newly created auth token
		 * */
		suspend fun registerToken(user: String): String {
			val id = IdentifierService.generate()
			val generatedToken = RandomService.generateString()

			val user = UserService.get(UserTable.id eq user)!!

			suspendTransaction {
				AuthEntity.new(id) {
					token = generatedToken
					this.user = user
				}
			}

			return generatedToken
		}
	}
}
