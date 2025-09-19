package site.remlit.blueb.aster.model

import kotlinx.serialization.Serializable

@Serializable
data class NodeInfoSoftware(
	val name: String,
	val version: String,
)
