package me.blueb.model.ap

import kotlinx.serialization.Serializable

@Serializable
data class ApKey(
	val id: String,
	val type: ApType.Object = ApType.Object.Key,
	val owner: String,
	val publicKeyPem: String,
)

