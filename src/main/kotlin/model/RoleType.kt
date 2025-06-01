package site.remlit.blueb.model

import kotlinx.serialization.Serializable

@Serializable
enum class RoleType {
	Admin, Mod, Normal
}
