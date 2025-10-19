package site.remlit.blueb.aster.model.plugin

import kotlinx.serialization.Serializable

@Serializable
data class PluginManifest(
	val name: String,
	val mainClass: String,
    val version: String,
	val authors: List<String>,
)
