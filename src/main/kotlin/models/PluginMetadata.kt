package me.blueb.models

import kotlinx.serialization.Serializable

@Serializable
data class PluginMetadata(
    val id: String,
    val version: String,
    val type: String
)
