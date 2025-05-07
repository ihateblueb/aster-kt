package me.blueb

import io.ktor.server.application.*
import kotlinx.coroutines.selects.select
import me.blueb.services.ConfigService
import me.blueb.services.IdentifierService
import me.blueb.services.UserService
import org.jetbrains.exposed.sql.Database

import org.koin.ktor.plugin.Koin
import org.koin.dsl.*
import org.koin.logger.slf4jLogger

fun Application.configureDatabases() {
    val configService = ConfigService()

    val database = Database.connect(
        "jdbc:postgresql://${configService.database.host}:${configService.database.port}/${configService.database.db}",
        user = configService.database.user,
        password = configService.database.password,
    )

    val userService = UserService(database)
    val identifierService = IdentifierService()

    install(Koin) {
        slf4jLogger()
        modules(module {
            single { configService }
            single { database }
            single { userService }
            single { identifierService }
        })
    }
}
