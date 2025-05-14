package me.blueb.service

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.blueb.db.entity.AuthEntity
import me.blueb.db.suspendTransaction
import java.math.BigInteger
import java.security.SecureRandom

class AuthService {
	private val identifierService = IdentifierService()

	fun generateToken() : String {
		val random = SecureRandom()

		val bytes = ByteArray(16)
		random.nextBytes(bytes)

		return BigInteger(1, bytes)
			.toString(32)
			.padStart(16, '0')
	}

	suspend fun registerToken(userId: String) : String {
		val id = identifierService.generate()
		val generatedToken = generateToken()

		suspendTransaction {
			AuthEntity.new(id) {
				token = generatedToken
				//user = userId
				createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
			}
		}

		return generatedToken
	}
}
