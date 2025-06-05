package site.remlit.blueb.aster.model

import kotlinx.serialization.Serializable

@Serializable
data class MetaStaff(
	val admin: List<User?> = listOf(),
	val mod: List<User?> = listOf()
)
