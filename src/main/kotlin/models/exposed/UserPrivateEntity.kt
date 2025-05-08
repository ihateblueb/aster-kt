package me.blueb.models.exposed

import kotlinx.serialization.Serializable

@Serializable
data class UserPrivateEntity(
    val id: String = "",
    val password: String = "",
)
