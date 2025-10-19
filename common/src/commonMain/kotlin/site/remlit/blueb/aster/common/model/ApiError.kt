package site.remlit.blueb.aster.common.model

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class ApiError(
	val message: String? = null,
	val callId: String? = null,
	val stackTrace: String? = null,
)