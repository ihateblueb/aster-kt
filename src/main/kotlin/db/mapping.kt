package me.blueb.db

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object UserTable : IdTable<String>("user") {
    override val id: Column<EntityID<String>> = varchar("id", length = 100).uniqueIndex("unique_user_id").entityId()

    val apId = varchar("apId", length = 8192).uniqueIndex("unique_user_apId")
    val inbox = varchar("inbox", length = 8192).uniqueIndex("unique_user_inbox")
    val outbox = varchar("outbox", length = 8192).nullable()

    val username = varchar("username", length = 175)
    val host = varchar("host", length = 500).nullable()
    val displayName = varchar("displayName", length = 500).nullable()
    val bio = varchar("bio", length = 8192).nullable()
    val location = varchar("location", length = 500).nullable()
    val birthday = varchar("birthday", length = 500).nullable()

    val avatar = varchar("avatar", length = 500).nullable()
    val avatarAlt = varchar("avatarAlt", length = 8192).nullable()
    val banner = varchar("banner", length = 500).nullable()
    val bannerAlt = varchar("bannerAlt", length = 8192).nullable()

    val locked = bool("locked").default(false)
    val suspended = bool("suspended").default(false)
    val activated = bool("activated").default(false)
    val discoverable = bool("discoverable").default(false)
    val indexable = bool("indexable").default(false)
    val sensitive = bool("sensitive").default(false)

    val isCat = bool("isCat").default(false)
    val speakAsCat = bool("speakAsCat").default(false)

    override val primaryKey = PrimaryKey(id)
}

object UserPrivateTable : IdTable<String>("user_private") {
    override val id = varchar("id", length = 100).uniqueIndex("unique_user_private_id").entityId()
    val password = varchar("password", length = 8192)

    override val primaryKey = PrimaryKey(UserTable.id)
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)