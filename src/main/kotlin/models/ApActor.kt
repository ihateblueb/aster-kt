package me.blueb.models

import kotlinx.serialization.*
import me.blueb.models.exposed.ExposedUser

@Serializable
data class ApActor(
    @SerialName("@context")
    val context: List<LdContextItem> = LdContext,

    val id: String,
    val type: ApType.Object = ApType.Object.Person,

    val url: String? = null,
    val preferredUsername: String,
    val name: String? = null,
) {
    companion object {
        fun fromUser(user: ExposedUser): ApActor {
            return ApActor(
                id = user.apId,
                type = ApType.Object.Person,
                preferredUsername = user.username,
            )
        }
    }
}