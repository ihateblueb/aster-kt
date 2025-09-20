package site.remlit.blueb.aster.db.entity

import kotlinx.datetime.LocalDateTime
import org.ktorm.entity.Entity

/**
 * Table for storing user information.
 *
 * @since 2025.9.1.2-SNAPSHOT
 * */
interface UserEntity : Entity<UserEntity> {
	var id: String

	var apId: String
	var inbox: String
	var outbox: String?

	var username: String
	var host: String?
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

	var publicKey: String

	var createdAt: LocalDateTime
	var updatedAt: LocalDateTime?

	var roles: List<String>
	var emojis: List<String>

	var followingUrl: String?
	var followersUrl: String?

	companion object : Entity.Factory<UserEntity>()
}