package site.remlit.blueb.aster.common.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class SmallUser(
	val id: String,

	val apId: String,

	val username: String,
	val host: String? = null,
	val displayName: String? = null,

	val avatar: String? = null,
	val avatarAlt: String? = null,

	val automated: Boolean = false,
	val sensitive: Boolean = false,

	val isCat: Boolean = false,

	val createdAt: LocalDateTime,
	val updatedAt: LocalDateTime? = null
) {
	companion object {
		fun fromUser(user: User): SmallUser {
			return SmallUser(
				id = user.id,
				apId = user.apId,
				username = user.username,
				host = user.host,
				displayName = user.displayName,
				avatar = user.avatar,
				avatarAlt = user.avatarAlt,
				automated = user.automated,
				sensitive = user.sensitive,
				isCat = user.isCat,
				createdAt = user.createdAt,
				updatedAt = user.updatedAt
			)
		}
	}
}
