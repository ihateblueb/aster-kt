package site.remlit.blueb.model

import kotlinx.serialization.Serializable

@Serializable
data class NodeInfoSoftware(
	val name: String,
	val version: String,
)
