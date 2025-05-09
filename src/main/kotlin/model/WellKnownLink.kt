package me.blueb.model

import kotlinx.serialization.Serializable

@Serializable
data class WellKnownLink(
    val rel: String,
    val href: String,
    val type: String? = null,
    val template: String? = null,
)