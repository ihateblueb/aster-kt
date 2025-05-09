package me.blueb.service

import io.ktor.http.Url
import io.ktor.server.config.ApplicationConfig
import me.blueb.model.InstanceRegistrationsMode

val config =
    ApplicationConfig(
        configPath = "application.yaml",
    )

class ConfigService {
    val name: String = config.property("name").getString()
    val version: String = config.property("version").getString()

    val url: Url = Url(config.property("url").getString())
    val registrations: InstanceRegistrationsMode = InstanceRegistrationsMode.valueOf(config.property("registrations").getString())

    val database: ConfigServiceDatabase = ConfigServiceDatabase()
}

class ConfigServiceDatabase {
    val host: String = config.property("database.host").getString()
    val port: String = config.property("database.port").getString()
    val db: String = config.property("database.db").getString()
    val user: String = config.property("database.user").getString()
    val password: String = config.property("database.password").getString()
}
