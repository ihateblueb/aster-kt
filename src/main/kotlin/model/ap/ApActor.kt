package me.blueb.model.ap

import kotlinx.serialization.Serializable
import me.blueb.db.entity.UserEntity

@Serializable
data class ApActor(
    val id: String,
    val type: ApType.Object = ApType.Object.Person,
    val url: String? = null,

    val preferredUsername: String,
    val name: String? = null,

    val summary: String? = null,
    val _misskey_summary: String? = null,
) : ApObjectWithContext() {
    companion object {
        fun fromEntity(user: UserEntity): ApActor =
            ApActor(
                id = user.apId,
                type = ApType.Object.Person,
                preferredUsername = user.username,
                summary = user.bio,
                _misskey_summary = user.bio
            )
    }
}