package site.remlit.blueb.aster.service

import site.remlit.blueb.aster.model.KeyType
import site.remlit.blueb.aster.model.Service
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*

class KeypairService : Service() {
	companion object {
        /**
         * Generate an RSA keypair.
         *
         * @return Generated keypair
         * */
		fun generate(): KeyPair {
			val generator = KeyPairGenerator.getInstance("RSA")
			generator.initialize(2048)
			return generator.generateKeyPair()
		}

        /**
         * Convert a key to a PEM format string.
         *
         * @param type Type of key
         * @param key Keypair to get key from
         *
         * @return PEM format string of key
         * */
		fun keyToPem(type: KeyType, key: KeyPair): String {
			val base64Key =
				Base64.getEncoder()
					.encodeToString(if (type == KeyType.Private) key.private.encoded else key.public.encoded)

			val start = if (type == KeyType.Private) "-----BEGIN PRIVATE KEY-----\n" else "-----BEGIN PUBLIC KEY-----\n"
			val end = if (type == KeyType.Private) "\n-----END PRIVATE KEY-----\n" else "\n-----END PUBLIC KEY-----\n"

			return start +
				base64Key.replace("(.{64})".toRegex(), "$1\n") +
				end
		}

        /**
         * Convert a PEM format string to a public key.
         *
         * @param pem PEM format string to convert
         *
         * @return Public key from a PEM format string
         * */
		fun pemToPublicKey(pem: String): PublicKey {
			val start = "-----BEGIN PUBLIC KEY-----\n"
			val end = "\n-----END PUBLIC KEY-----\n"

			val editedString = pem.replace(start, "").replace(end, "").replace("\n", "")
			val byteArray = Base64.getDecoder().decode(editedString)

			return KeyFactory.getInstance("RSA").generatePublic(X509EncodedKeySpec(byteArray))
		}
	}
}
