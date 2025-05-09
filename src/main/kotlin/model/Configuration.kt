package me.blueb.model

import io.ktor.http.Url
import io.ktor.server.config.ApplicationConfig
import java.util.Locale
import java.util.Locale.getDefault

val config =
    ApplicationConfig(
        configPath = "application.yaml",
    )

class Configuration {
    val name: String = config.property("name").getString()

    val url: Url = Url(config.property("url").getString())
    val registrations: InstanceRegistrationsMode = InstanceRegistrationsMode.valueOf(config.property("registrations").getString()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() })
    val identifiers: IdentifierType = IdentifierType.valueOf(config.property("identifiers").getString()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() })

    val software: ConfigurationSoftware = ConfigurationSoftware()
    val database: ConfigurationDatabase = ConfigurationDatabase()
}

class ConfigurationSoftware {
    val name: String = config.property("software.name").getString()
    val version: String = config.property("software.version").getString()
}

class ConfigurationDatabase {
    val host: String = config.property("database.host").getString()
    val port: String = config.property("database.port").getString()
    val db: String = config.property("database.db").getString()
    val user: String = config.property("database.user").getString()
    val password: String = config.property("database.password").getString()
}
