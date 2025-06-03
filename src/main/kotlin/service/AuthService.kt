package site.remlit.blueb.service

import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import site.remlit.blueb.db.entity.AuthEntity
import site.remlit.blueb.db.entity.UserEntity
import site.remlit.blueb.db.suspendTransaction
import site.remlit.blueb.db.table.AuthTable

class AuthService {
	private val identifierService = IdentifierService()
	private val randomService = RandomService()

	suspend fun get(where: Op<Boolean>): AuthEntity? = suspendTransaction {
		AuthEntity
			.find { where }
			.singleOrNull()
	}

	suspend fun getByToken(token: String): AuthEntity? = get(AuthTable.token eq token)

	suspend fun registerToken(userEntity: UserEntity): String {
		val id = identifierService.generate()
		val generatedToken = randomService.generateString()

		suspendTransaction {
			AuthEntity.new(id) {
				token = generatedToken
				user = userEntity
			}
		}

		return generatedToken
	}
}
