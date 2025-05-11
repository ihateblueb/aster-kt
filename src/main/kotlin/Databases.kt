package me.blueb

import io.ktor.server.application.*

import me.blueb.service.*
import me.blueb.db.*

import MigrationUtils
import me.blueb.db.table.NoteTable
import me.blueb.db.table.UserPrivateTable
import me.blueb.db.table.UserTable
import me.blueb.model.Configuration
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.sql.transactions.transaction

const val migrationPath: String = "src/main/kotlin/migration"

@OptIn(ExperimentalDatabaseMigrationApi::class)
fun Application.configureDatabases() {
    val configuration = Configuration()
    val identifierService = IdentifierService()

    val dbUrl = "jdbc:postgresql://${configuration.database.host}:${configuration.database.port}/${configuration.database.db}"

    val database = Database.connect(
        dbUrl,
        user = configuration.database.user,
        password = configuration.database.password,
    )

    val flyway = Flyway.configure()
        .dataSource(dbUrl, configuration.database.user, configuration.database.password)
        .locations("filesystem:$migrationPath")
        .load()

    /*transaction(database) {
        val tables = listOf(
            UserTable,
            UserPrivateTable,
            NoteTable,
        )

        for (table in tables) {
            log.info("Generating migration script for table " + table.tableName)
            MigrationUtils.generateMigrationScript(
                table,
                scriptDirectory = migrationPath,
                scriptName = "V${System.currentTimeMillis()}__Migration_${table.tableName}_${identifierService.generate()}",
            )
        }
    }

    transaction(database) {
        flyway.migrate()
    }*/
}
