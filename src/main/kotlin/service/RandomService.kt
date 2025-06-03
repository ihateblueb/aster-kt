package site.remlit.blueb.service

import java.math.BigInteger
import java.security.SecureRandom

class RandomService {
	fun generateString(size: Int = 16): String {
		val random = SecureRandom()

		val bytes = ByteArray(size)
		random.nextBytes(bytes)

		return BigInteger(1, bytes)
			.toString(32)
			.padStart(size, '0')
	}
}
