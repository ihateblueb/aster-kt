package site.remlit.blueb.aster.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
	val message: String? = null,
	val callId: String? = null,
	val stackTrace: String? = null,
)
