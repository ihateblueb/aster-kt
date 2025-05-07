package me.blueb.models

import kotlinx.serialization.Serializable

@Serializable
data class ExposedUserPrivate(
    val id: String = "",
    val password: String = "",
)