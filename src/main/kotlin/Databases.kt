package me.blueb

import io.ktor.server.application.*
import kotlinx.coroutines.selects.select
import me.blueb.services.ConfigService
import me.blueb.services.IdentifierService
import me.blueb.services.UserService
import me.blueb.tools.MigrationUtils
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

import org.koin.ktor.plugin.Koin
import org.koin.dsl.*
import org.koin.logger.slf4jLogger

fun Application.configureDatabases(migrate: Boolean = false) {
    val configService = ConfigService()

    val database = Database.connect(
        "jdbc:postgresql://${configService.database.host}:${configService.database.port}/${configService.database.db}",
        user = configService.database.user,
        password = configService.database.password,
    )

    if (migrate) {
        MigrationUtils().runPendingMigrations(database)
        return
    }

    val userService = UserService(database)
    val identifierService = IdentifierService()

    install(Koin) {
        slf4jLogger()
        modules(module {
            single { configService }
            single { userService }
            single { identifierService }
        })
    }
}
