package site.remlit.blueb.aster.util.model

import site.remlit.blueb.aster.common.model.SmallUser
import site.remlit.blueb.aster.common.model.User
import site.remlit.blueb.aster.db.entity.UserEntity
import kotlin.collections.map


fun User.Companion.fromEntity(entity: UserEntity) = User(
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

	createdAt = entity.createdAt,
	updatedAt = entity.updatedAt,

	publicKey = entity.publicKey
)

fun User.Companion.fromEntities(entities: List<UserEntity>): List<User> =
    entities.map { fromEntity(it) }

/**
 * Convert a user to a small user.
 * */
fun User.toSmall(): SmallUser = SmallUser.fromUser(this)

/**
 * Convert a list of users to a list of small users.
 * */
fun List<User>.toSmall(): List<SmallUser> = this.map { it.toSmall() }