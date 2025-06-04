package site.remlit.blueb.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
	val token: String,
	val user: User
)
