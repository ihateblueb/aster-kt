package site.remlit.aster.common.model.response

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class AuthResponse(
	val token: String,
	val user: site.remlit.aster.common.model.User
)