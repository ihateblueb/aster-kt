package me.blueb.service

import me.blueb.model.KeyType
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.util.Base64

class KeypairService {
	fun generate(): KeyPair {
		val generator = KeyPairGenerator.getInstance("RSA")
		generator.initialize(2048)
		return generator.generateKeyPair()
	}

	fun keyToPem(type: KeyType, key: KeyPair): String {
		val base64Key = Base64.getEncoder().encodeToString(if (type == KeyType.Private) key.private.encoded else key.public.encoded)

		val start = if (type == KeyType.Private) "-----BEGIN PRIVATE KEY-----\n" else "-----BEGIN PUBLIC KEY-----\n"
		val end = if (type == KeyType.Private) "\n-----END PRIVATE KEY-----\n" else "\n-----END PUBLIC KEY-----\n"

		return start +
			base64Key.replace("(.{64})".toRegex(), "$1\n") +
			end
	}
}
