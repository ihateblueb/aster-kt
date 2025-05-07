package me.blueb.tools

import kotlinx.coroutines.Dispatchers
import me.blueb.services.UserService
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.exposedLogger
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class MigrationUtils {
    object Migrations : Table() {
        val id = varchar("id", length = 100).uniqueIndex("unique_migration_id")
        val executedAt = timestamp("executedAt").nullable()

        val unique_migration_id = uniqueIndex("unique_migration_id", id)

        override val primaryKey = PrimaryKey(id)
        override val tableName = "migration"
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    fun runPendingMigrations(database: Database) {
        exposedLogger.info("Executing migrations")

        transaction(database) {
            val tables = SchemaUtils.listTables()

            if (tables.isEmpty() || "migration" !in tables) {
                exposedLogger.info("Creating migrations table")
                SchemaUtils.create(Migrations)
            }
        }
    }
}