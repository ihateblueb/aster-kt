package site.remlit.blueb.aster.common.model.response

import kotlinx.serialization.Serializable
import site.remlit.blueb.aster.common.model.User
import kotlin.js.JsExport

@JsExport
@Serializable
data class AuthResponse(
	val token: String,
	val user: User
)