package site.remlit.aster.service

import site.remlit.aster.model.KeyType
import site.remlit.aster.model.Service
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

/**
 * Service for managing user public and private keys.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
object KeypairService : Service {
	/**
	 * Generates a keypair for a new user.
	 *
	 * @return Keypair
	 * */
	fun generate(): KeyPair {
		val generator = KeyPairGenerator.getInstance("RSA")
		generator.initialize(2048)
		return generator.generateKeyPair()
	}

	/**
	 * Converts a key to pem string.
	 *
	 * @param type Type of key
	 * @param key Keypair
	 *
	 * @return Pem version of key
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
	 * Converts a pem string to a public key.
	 *
	 * @param pem Pem string
	 *
	 * @return Public key
	 * */
	fun pemToPublicKey(pem: String): PublicKey {
		val start = "-----BEGIN PUBLIC KEY-----\n"
		val end = "\n-----END PUBLIC KEY-----\n"

		val editedString = pem.replace(start, "")
			.replace(end, "")
			.replace("\n", "")
			.trim()

		val byteArray = Base64.getMimeDecoder().decode(editedString)

		return KeyFactory.getInstance("RSA").generatePublic(X509EncodedKeySpec(byteArray))
	}

	/**
	 * Converts a pem string to a private key.
	 *
	 * @param pem Pem string
	 *
	 * @return Private key
	 * */
	fun pemToPrivateKey(pem: String): PrivateKey {
		val start = "-----BEGIN PRIVATE KEY-----\n"
		val end = "\n-----END PRIVATE KEY-----\n"

		val editedString = pem.replace(start, "")
			.replace(end, "")
			.replace("\n", "")
			.trim()
		
		val byteArray = Base64.getMimeDecoder().decode(editedString)

		return KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(byteArray))
	}
}
