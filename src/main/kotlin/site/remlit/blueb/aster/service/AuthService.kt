package site.remlit.blueb.aster.service

import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import site.remlit.blueb.aster.db.entity.AuthEntity
import site.remlit.blueb.aster.db.suspendTransaction
import site.remlit.blueb.aster.db.table.AuthTable
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.model.Service

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
			val id = _root_ide_package_.site.remlit.blueb.aster.service.IdentifierService.Companion.generate()
			val generatedToken = _root_ide_package_.site.remlit.blueb.aster.service.RandomService.Companion.generateString()

			val user = _root_ide_package_.site.remlit.blueb.aster.service.UserService.Companion.get(UserTable.id eq user)!!

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
