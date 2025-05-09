package me.blueb

import io.ktor.server.application.*

import me.blueb.service.*
import me.blueb.db.*

import MigrationUtils
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.sql.transactions.transaction

const val migrationPath: String = "src/main/kotlin/migration"

@OptIn(ExperimentalDatabaseMigrationApi::class)
fun Application.configureDatabases() {
    val configService = ConfigService()
    val identifierService = IdentifierService()

    val dbUrl = "jdbc:postgresql://${configService.database.host}:${configService.database.port}/${configService.database.db}"

    val database = Database.connect(
        dbUrl,
        user = configService.database.user,
        password = configService.database.password,
    )

    val flyway = Flyway.configure()
        .dataSource(dbUrl, configService.database.user, configService.database.password)
        .locations("filesystem:$migrationPath")
        .load()

    transaction(database) {
        val entities = listOf(UserTable, UserPrivateTable)

        for (entity in entities) {
            log.info("Generating migration script for table " + entity.tableName)
            MigrationUtils.generateMigrationScript(
                entity,
                scriptDirectory = migrationPath,
                scriptName = "V${System.currentTimeMillis()}__Migration_${entity.tableName}_${identifierService.generate()}",
            )
        }
    }

    transaction(database) {
        flyway.migrate()
    }
}
