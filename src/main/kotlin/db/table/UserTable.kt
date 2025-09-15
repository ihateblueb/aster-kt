package site.remlit.blueb.aster.db.table

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.timestamp
import org.ktorm.schema.varchar
import site.remlit.blueb.aster.db.entity.UserEntity

class UserTable : Table<UserEntity>("user") {
	val id = varchar("id").primaryKey()

	val apId = varchar("apId")
	val inbox = varchar("inbox")
	val outbox = varchar("outbox")

	val username = varchar("username")
	val host = varchar("host")
	val displayName = varchar("displayName")

	val bio = varchar("bio")
	val location = varchar("location")
	val birthday = varchar("birthday")

	val avatar = varchar("avatar")
	val avatarAlt = varchar("avatarAlt")
	val banner = varchar("banner")
	val bannerAlt = varchar("bannerAlt")

	val locked = boolean("locked")
	val suspended = boolean("suspended")
	val activated = boolean("activated")
	val discoverable = boolean("discoverable")
	val indexable = boolean("indexable")
	val sensitive = boolean("sensitive")
	val automated = boolean("automated")
	val isCat = boolean("isCat")
	val speakAsCat = boolean("speakAsCat")

	val publicKey = varchar("publicKey")

	val createdAt = timestamp("createdAt")
	val updatedAt = timestamp("updatedAt")

	// todo: roles
	// todo: emojis

	val followingUrl = varchar("followingUrl")
	val followersUrl = varchar("followersUrl")


}