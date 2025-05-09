package me.blueb.model.entity

import kotlinx.serialization.Serializable
import me.blueb.db.UserTable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class UserEntity(
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
) {
    companion object {
        fun fromTable(it: ResultRow): UserEntity {
            return UserEntity(
                id = it[UserTable.id].toString(),
                apId = it[UserTable.apId],
                inbox = it[UserTable.inbox],
                outbox = it[UserTable.outbox],
                username = it[UserTable.username],
                host = it[UserTable.host],
                displayName = it[UserTable.displayName],
                bio = it[UserTable.bio],
                location = it[UserTable.location],
                birthday = it[UserTable.birthday],
                avatar = it[UserTable.avatar],
                avatarAlt = it[UserTable.avatarAlt],
                banner = it[UserTable.banner],
                bannerAlt = it[UserTable.bannerAlt],
                locked = it[UserTable.locked],
                suspended = it[UserTable.suspended],
                activated = it[UserTable.activated],
                discoverable = it[UserTable.discoverable],
                indexable = it[UserTable.indexable],
                sensitive = it[UserTable.sensitive],
                isCat = it[UserTable.isCat],
                speakAsCat = it[UserTable.speakAsCat],
            )
        }
    }
}
