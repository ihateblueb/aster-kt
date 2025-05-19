package me.blueb.service

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.blueb.db.entity.AuthEntity
import me.blueb.db.entity.UserEntity
import me.blueb.db.suspendTransaction
import me.blueb.db.table.AuthTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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

	fun generateToken() : String {
		val random = SecureRandom()

		val bytes = ByteArray(16)
		random.nextBytes(bytes)

		return BigInteger(1, bytes)
			.toString(32)
			.padStart(16, '0')
	}

	suspend fun registerToken(userEntity: UserEntity) : String {
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
