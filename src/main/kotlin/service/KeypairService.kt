package site.remlit.blueb.aster.service

import site.remlit.blueb.aster.model.KeyType
import site.remlit.blueb.aster.model.Service
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*

/**
 * Service for managing user public and private keys.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
class KeypairService : Service() {
	companion object {
		fun generate(): KeyPair {
			val generator = KeyPairGenerator.getInstance("RSA")
			generator.initialize(2048)
			return generator.generateKeyPair()
		}

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

		fun pemToPublicKey(pem: String): PublicKey {
			val start = "-----BEGIN PUBLIC KEY-----\n"
			val end = "\n-----END PUBLIC KEY-----\n"

			val editedString = pem.replace(start, "").replace(end, "").replace("\n", "")
			val byteArray = Base64.getDecoder().decode(editedString)

			return KeyFactory.getInstance("RSA").generatePublic(X509EncodedKeySpec(byteArray))
		}
	}
}
