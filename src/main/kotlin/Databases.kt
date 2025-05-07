package me.blueb

import io.ktor.server.application.*
import me.blueb.services.*

import MigrationUtils
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.sql.transactions.transaction

import org.koin.dsl.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

const val migrationPath: String = "src/main/kotlin/migrations"

@OptIn(ExperimentalDatabaseMigrationApi::class)
fun Application.configureDatabases(
    migrate: Boolean = false,
    genMigration: Boolean = false,
) {
    val configService = ConfigService()

    val dbUrl = "jdbc:postgresql://${configService.database.host}:${configService.database.port}/${configService.database.db}"

    val database =
        Database.connect(
            dbUrl,
            user = configService.database.user,
            password = configService.database.password,
        )

    val userService = UserService(database)
    val identifierService = IdentifierService()

    val flyway = Flyway.configure()
        .dataSource(dbUrl, configService.database.user, configService.database.password)
        .locations("filesystem:$migrationPath")
        .load()

    transaction(database) {
        val entities = listOf(UserService.Users, UserService.UsersPrivate)

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

    install(Koin) {
        slf4jLogger()
        modules(
            module {
                single { configService }
                single { database }
                single { userService }
                single { identifierService }
            },
        )
    }
}
