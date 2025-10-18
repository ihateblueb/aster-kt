package site.remlit.blueb.aster.common.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class RoleType {
	@SerialName("admin")
	Admin,

	@SerialName("mod")
	Mod,

	@SerialName("normal")
	Normal
}
