package me.blueb.models.exposed

import kotlinx.serialization.Serializable

@Serializable
data class ExposedUser(
    val id: String,
    val apId: String,
    val inbox: String,
    val outbox: String? = null,
    val username: String,
    val host: String? = null,
    val displayName: String? = null,
    val bio: String? = null,
    val location: String? = null,
    val birthday: String? = null,
    val avatar: String? = null,
    val avatarAlt: String? = null,
    val banner: String? = null,
    val bannerAlt: String? = null,
    val locked: Boolean = false,
    val suspended: Boolean = false,
    val activated: Boolean = false,
    val discoverable: Boolean = false,
    val indexable: Boolean = false,
    val sensitive: Boolean = false,
    val isCat: Boolean = false,
    val speakAsCat: Boolean = false,
)
