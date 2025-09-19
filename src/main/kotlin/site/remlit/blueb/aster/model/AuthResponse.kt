package site.remlit.blueb.aster.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
	val token: String,
	val user: User
)
