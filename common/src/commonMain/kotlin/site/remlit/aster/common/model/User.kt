package site.remlit.aster.common.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
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

	val followersUrl: String? = null,
	val followingUrl: String? = null,

	val createdAt: LocalDateTime,
	val updatedAt: LocalDateTime? = null,

	val publicKey: String
)