package site.remlit.blueb.aster.service

import site.remlit.blueb.aster.model.Service
import java.math.BigInteger
import java.security.SecureRandom

class RandomService : Service() {
	companion object {
		/**
		 * Securely generate a random string
		 *
		 * @param size Size in bytes
		 *
		 * @return Secure random string
		 * */
		fun generateString(size: Int = 16): String {
			val random = SecureRandom()

			val bytes = ByteArray(size)
			random.nextBytes(bytes)

			return BigInteger(1, bytes)
				.toString(32)
				.padStart(size, '0')
		}
	}
}
