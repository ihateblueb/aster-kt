package site.remlit.blueb.aster.common.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
enum class RoleType {
	@SerialName("admin")
	Admin,

	@SerialName("mod")
	Mod,

	@SerialName("normal")
	Normal
}
