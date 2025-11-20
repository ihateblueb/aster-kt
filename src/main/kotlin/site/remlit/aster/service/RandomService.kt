package site.remlit.aster.service

import site.remlit.aster.model.Service
import java.math.BigInteger
import java.security.SecureRandom

/**
 * Service for creating truly random strings.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
object RandomService : Service {
	/**
	 * Securely generate a random string
	 *
	 * @param size Size in bytes
	 *
	 * @return Secure random string
	 * */
	@JvmStatic
	fun generateString(size: Int = 16): String {
		val random = SecureRandom()

		val bytes = ByteArray(size)
		random.nextBytes(bytes)

		return BigInteger(1, bytes)
			.toString(32)
			.padStart(size, '0')
	}
}
