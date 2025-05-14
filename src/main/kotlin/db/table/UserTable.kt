package me.blueb.db.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object UserTable : IdTable<String>("user") {
    override val id: Column<EntityID<String>> = varchar("id", length = 125).uniqueIndex("unique_user_id").entityId()

    val apId = varchar("apId", length = 1025).uniqueIndex("unique_user_apId")
    val inbox = varchar("inbox", length = 1025).uniqueIndex("unique_user_inbox")
    val outbox = varchar("outbox", length = 1025).nullable()

    val username = varchar("username", length = 250)
    val host = varchar("host", length = 500).nullable()
    val displayName = varchar("displayName", length = 500).nullable()
    val bio = varchar("bio", length = 25000).nullable()
    val location = varchar("location", length = 1025).nullable()
    val birthday = varchar("birthday", length = 1025).nullable()

    val avatar = varchar("avatar", length = 1025).nullable()
    val avatarAlt = varchar("avatarAlt", length = 25000).nullable()
    val banner = varchar("banner", length = 1025).nullable()
    val bannerAlt = varchar("bannerAlt", length = 25000).nullable()

    val locked = bool("locked").default(false)
    val suspended = bool("suspended").default(false)
    val activated = bool("activated").default(false)
    val automated = bool("automated").default(false)
    val discoverable = bool("discoverable").default(false)
    val indexable = bool("indexable").default(false)
    val sensitive = bool("sensitive").default(false)

    val isCat = bool("isCat").default(false)
    val speakAsCat = bool("speakAsCat").default(false)

	val createdAt = datetime("createdAt")
	val updatedAt = datetime("updatedAt").nullable()

	val publicKey = varchar("publicKey", length = 5000)

    override val primaryKey = PrimaryKey(NoteTable.id)
}
