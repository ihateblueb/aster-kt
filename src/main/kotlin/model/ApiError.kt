package site.remlit.blueb.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
	val message: String? = null,
	val callId: String? = null
)
