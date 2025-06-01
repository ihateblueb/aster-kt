package site.remlit.blueb.db.entity

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import site.remlit.blueb.db.table.UserTable

class UserEntity(id: EntityID<String>) : Entity<String>(id) {
	companion object : EntityClass<String, UserEntity>(UserTable)

	var apId by UserTable.apId
	var inbox by UserTable.inbox
	var outbox by UserTable.outbox

	var username by UserTable.username
	var host by UserTable.host
	var displayName by UserTable.displayName
	var bio by UserTable.bio
	var location by UserTable.location
	var birthday by UserTable.birthday

	var avatar by UserTable.avatar
	var avatarAlt by UserTable.avatarAlt
	var banner by UserTable.banner
	var bannerAlt by UserTable.bannerAlt

	var locked by UserTable.locked
	var suspended by UserTable.suspended
	var activated by UserTable.activated
	var automated by UserTable.automated
	var discoverable by UserTable.discoverable
	var indexable by UserTable.indexable
	var sensitive by UserTable.sensitive

	var roles by UserTable.roles
	var emojis by UserTable.emojis

	var isCat by UserTable.isCat
	var speakAsCat by UserTable.speakAsCat

	var createdAt by UserTable.createdAt
	var updatedAt by UserTable.updatedAt

	var publicKey by UserTable.publicKey
}
