package site.remlit.blueb.aster.db.entity

import kotlinx.datetime.LocalDateTime
import org.ktorm.entity.Entity

interface UserEntity : Entity<UserEntity> {
	val id: String

	val apId: String
	val inbox: String
	val outbox: String?

	val username: String
	val host: String?
	var displayName: String?

	var bio: String?
	var location: String?
	var birthday: String?

	var avatar: String?
	var avatarAlt: String?
	var banner: String?
	var bannerAlt: String?

	var locked: Boolean
	var suspended: Boolean
	var activated: Boolean
	var discoverable: Boolean
	var indexable: Boolean
	var sensitive: Boolean
	var automated: Boolean
	var isCat: Boolean
	var speakAsCat: Boolean

	val publicKey: String

	val createdAt: LocalDateTime
	var updatedAt: LocalDateTime?

	val followingUrl: String?
	val followersUrl: String?
}