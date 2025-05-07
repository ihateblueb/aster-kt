package me.blueb.models

import kotlinx.serialization.*
import me.blueb.models.exposed.ExposedUser

@Serializable
data class ApActor(
    @Contextual
    @SerialName("@context")
    val context: List<Any> = listOf(
        "https://www.w3.org/ns/activitystreams",
        "https://w3id.org/security/v1",
        LdContext()
    ),

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