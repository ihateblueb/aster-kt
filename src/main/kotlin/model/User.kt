package me.blueb.model

import kotlinx.serialization.Serializable
import me.blueb.db.entity.NoteEntity
import me.blueb.db.entity.UserEntity

@Serializable
data class User(
    val id: String,

    val apId: String,
    val inbox: String,
    val outbox: String?,

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
    val automated: Boolean = false,
    val discoverable: Boolean = false,
    val indexable: Boolean = false,
    val sensitive: Boolean = false,

    val isCat: Boolean = false,
    val speakAsCat: Boolean = false,

	val publicKey: String,
) {
    companion object {
        fun fromEntity(entity: UserEntity) = User(
            id = entity.id.toString(),
            apId = entity.apId,
            inbox = entity.inbox,
            outbox = entity.outbox,

            username = entity.username,
            host = entity.host,
            displayName = entity.displayName,
            bio = entity.bio,
            location = entity.location,
            birthday = entity.birthday,

            avatar = entity.avatar,
            avatarAlt = entity.avatarAlt,
            banner = entity.banner,
            bannerAlt = entity.bannerAlt,

            locked = entity.locked,
            suspended = entity.suspended,
            activated = entity.activated,
            automated = entity.automated,
            discoverable = entity.discoverable,
            indexable = entity.indexable,
            sensitive = entity.sensitive,

            isCat = entity.isCat,
            speakAsCat = entity.speakAsCat,

			publicKey = entity.publicKey,
        )
    }
}
