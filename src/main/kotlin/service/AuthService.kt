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
		suspend fun get(where: Op<Boolean>): AuthEntity? = suspendTransaction {
			AuthEntity
				.find { where }
				.singleOrNull()
		}

		suspend fun getByToken(token: String): AuthEntity? = get(AuthTable.token eq token)

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
