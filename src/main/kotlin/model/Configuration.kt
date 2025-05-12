package me.blueb.model

import io.ktor.http.Url
import io.ktor.server.config.yaml.YamlConfig
import java.io.File
import java.util.Locale.getDefault

var workingDir = File(".").absolutePath.toString().removeSuffix(".")
var configPath = workingDir + "configuration.yaml"

val config = YamlConfig(configPath)

class Configuration {
    val name: String

    val url: Url
    val registrations: InstanceRegistrationsMode
    val identifiers: IdentifierType

    val database: ConfigurationDatabase

	init {
		fun lower(string: String): String {
			return string
				.replaceFirstChar {
					if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString()
				}
		}

		var nameProp = config?.propertyOrNull("name")?.getString()
		name = nameProp ?: "Aster"
		
		var urlProp = config?.propertyOrNull("url")?.getString()
		url = if (urlProp != null) {
			Url(urlProp)
		} else {
			throw Exception("Configuration is missing 'url' attribute.")
		}

		var registrationsProp = config?.propertyOrNull("registrations")?.getString()
		registrations = if (registrationsProp != null) {
			InstanceRegistrationsMode.valueOf(lower(registrationsProp))
		} else {
			InstanceRegistrationsMode.Closed
		}

		var identifiersProp = config?.propertyOrNull("identifiers")?.getString()
		identifiers = if (identifiersProp != null) {
			IdentifierType.valueOf(lower(identifiersProp))
		} else {
			IdentifierType.Aidx
		}

		database = ConfigurationDatabase()
	}
}

class ConfigurationDatabase {
    val host: String
    val port: String
    val db: String
    val user: String
    val password: String

	init {
		var databaseHostProp = config?.propertyOrNull("database.host")?.getString()
		host = databaseHostProp ?: "127.0.0.1"

		var databasePortProp = config?.propertyOrNull("database.port")?.getString()
		port = databasePortProp ?: "5432"

		var databaseDbProp = config?.propertyOrNull("database.db")?.getString()
		db = if (databaseDbProp != null) {
			databaseDbProp
		} else {
			throw Exception("Configuration is missing 'database.db' attribute.")
		}

		var databaseUserProp = config?.propertyOrNull("database.user")?.getString()
		user = if (databaseUserProp != null) {
			databaseUserProp
		} else {
			throw Exception("Configuration is missing 'database.user' attribute.")
		}

		var databasePasswordProp = config?.propertyOrNull("database.password")?.getString()
		password = if (databasePasswordProp != null) {
			databasePasswordProp
		} else {
			throw Exception("Configuration is missing 'database.password' attribute.")
		}
	}
}
