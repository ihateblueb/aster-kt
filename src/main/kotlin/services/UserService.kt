package me.blueb.services

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

import me.blueb.models.exposed.ExposedUser
import me.blueb.models.exposed.ExposedUserPrivate

class UserService(private val database: Database) {
    object Users : Table("user") {
        val id = varchar("id", length = 100).uniqueIndex("unique_user_id")

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

        val unique_user_id = uniqueIndex("unique_user_id", id)
        val unique_user_apId = uniqueIndex("unique_user_apId", apId)
        val unique_user_inbox = uniqueIndex("unique_user_inbox", inbox)
        val unique_user_outbox = uniqueIndex("unique_user_outbox", outbox)

        override val primaryKey = PrimaryKey(id)
    }

    object UsersPrivate : Table("user_private") {
        val id = varchar("id", length = 100).uniqueIndex("unique_user_private_id")
        val password = varchar("password", length = 8192)

        override val primaryKey = PrimaryKey(Users.id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Users)
            SchemaUtils.create(UsersPrivate)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(user: ExposedUser, userPrivate: ExposedUserPrivate?): String = dbQuery {
        if (userPrivate != null)
            UsersPrivate.insert {
                it[id] = userPrivate.id
                it[password] = userPrivate.password
            }[Users.id]

        Users.insert {
            it[id] = user.id
            it[apId] = user.apId
            it[inbox] = user.inbox
            it[outbox] = user.outbox
            it[username] = user.username
            it[host] = user.host
            it[displayName] = user.displayName
            it[bio] = user.bio
            it[location] = user.location
            it[birthday] = user.birthday
            it[avatar] = user.avatar
            it[avatarAlt] = user.avatarAlt
            it[banner] = user.banner
            it[bannerAlt] = user.bannerAlt
            it[Users.locked] = user.locked
            it[Users.suspended] = user.suspended
            it[Users.activated] = user.activated
            it[Users.discoverable] = user.discoverable
            it[Users.indexable] = user.indexable
            it[Users.sensitive] = user.sensitive
            it[Users.isCat] = user.isCat
            it[Users.speakAsCat] = user.speakAsCat
        }[Users.id]
    }

    suspend fun read(id: String): ExposedUser? {
        return dbQuery {
            Users.selectAll()
                .where { Users.id eq id }
                .map {
                    ExposedUser(
                        it[Users.id],
                        it[Users.apId],
                        it[Users.inbox],
                        it[Users.outbox],
                        it[Users.username],
                        it[Users.host],
                        it[Users.displayName],
                        it[Users.bio],
                        it[Users.location],
                        it[Users.birthday],
                        it[Users.avatar],
                        it[Users.avatarAlt],
                        it[Users.banner],
                        it[Users.bannerAlt],
                        it[Users.locked],
                        it[Users.suspended],
                        it[Users.activated],
                        it[Users.discoverable],
                        it[Users.indexable],
                        it[Users.sensitive],
                        it[Users.isCat],
                        it[Users.speakAsCat],
                    )
                }
                .singleOrNull()
        }
    }

    suspend fun readPrivate(id: String): ExposedUserPrivate? {
        return dbQuery {
            UsersPrivate.selectAll()
                .where { Users.id eq id }
                .map {
                    ExposedUserPrivate(
                        it[UsersPrivate.id],
                        it[UsersPrivate.password]
                    )
                }
                .singleOrNull()
        }
    }

    suspend fun update(id: String, user: ExposedUser) {
        dbQuery {
            Users.update({ Users.id eq id }) {
                it[apId] = user.apId
                it[inbox] = user.inbox
                it[outbox] = user.outbox
                it[username] = user.username
                it[host] = user.host
                it[displayName] = user.displayName
                it[bio] = user.bio
                it[location] = user.location
                it[birthday] = user.birthday
                it[avatar] = user.avatar
                it[avatarAlt] = user.avatarAlt
                it[banner] = user.banner
                it[bannerAlt] = user.bannerAlt
                it[Users.locked] = user.locked
                it[Users.suspended] = user.suspended
                it[Users.activated] = user.activated
                it[Users.discoverable] = user.discoverable
                it[Users.indexable] = user.indexable
                it[Users.sensitive] = user.sensitive
                it[Users.isCat] = user.isCat
                it[Users.speakAsCat] = user.speakAsCat
            }
        }
    }

    suspend fun updatePrivate(id: String, userPrivate: ExposedUserPrivate) {
        dbQuery {
            UsersPrivate.update({ UsersPrivate.id eq id }) {
                it[password] = userPrivate.password
            }
        }
    }

    suspend fun delete(id: String) {
        dbQuery {
            Users.deleteWhere { Users.id.eq(id) }
            UsersPrivate.deleteWhere { UsersPrivate.id.eq(id) }
        }
    }
}