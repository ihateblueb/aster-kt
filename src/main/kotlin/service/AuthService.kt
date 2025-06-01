package site.remlit.blueb.service

import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import site.remlit.blueb.db.entity.AuthEntity
import site.remlit.blueb.db.entity.UserEntity
import site.remlit.blueb.db.suspendTransaction
import site.remlit.blueb.db.table.AuthTable
import java.math.BigInteger
import java.security.SecureRandom

class AuthService {
	private val identifierService = IdentifierService()

	suspend fun get(where: Op<Boolean>): AuthEntity? = suspendTransaction {
		AuthEntity
			.find { where }
			.singleOrNull()
	}

	suspend fun getByToken(token: String): AuthEntity? = get(AuthTable.token eq token)

	fun generateToken(): String {
		val random = SecureRandom()

		val bytes = ByteArray(16)
		random.nextBytes(bytes)

		return BigInteger(1, bytes)
			.toString(32)
			.padStart(16, '0')
	}

	suspend fun registerToken(userEntity: UserEntity): String {
		val id = identifierService.generate()
		val generatedToken = generateToken()

		suspendTransaction {
			AuthEntity.new(id) {
				token = generatedToken
				user = userEntity
			}
		}

		return generatedToken
	}
}
