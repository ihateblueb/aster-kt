package site.remlit.blueb.aster.service

import org.ktorm.dsl.from
import org.ktorm.dsl.limit
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.schema.ColumnDeclaring
import site.remlit.blueb.aster.db.Database.database
import site.remlit.blueb.aster.db.entity.AuthEntity
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
		suspend fun get(where: () -> ColumnDeclaring<Boolean>): AuthEntity? =
			database
				.from(AuthTable)
				.select()
				.where(where)
				.limit(1)
				.map { row -> AuthTable.createEntity(row) }
				.firstOrNull()

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
