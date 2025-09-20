package site.remlit.blueb.aster.model

import io.ktor.http.*
import io.ktor.server.config.yaml.*
import site.remlit.blueb.aster.exception.ConfigurationException
import java.io.File
import java.util.Locale.getDefault

var workingDir = File(".").absolutePath.toString().removeSuffix(".")
var configPath = workingDir + "configuration.yaml"

val config = YamlConfig(configPath)

@Suppress("MagicNumber")
class Configuration {
	val name: String

	val url: Url
	val port: Int
	val host: String
	val registrations: InstanceRegistrationsType
	val identifiers: IdentifierType

	val database: ConfigurationDatabase
	val queue: ConfigurationQueue
	val timeline: ConfigurationTimeline

	init {
		fun lower(string: String): String {
			return string
				.replaceFirstChar {
					if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString()
				}
		}

		val nameProp = config?.propertyOrNull("name")?.getString()
		name = nameProp ?: "Aster"

		val urlProp = config?.propertyOrNull("url")?.getString()
		url = if (urlProp != null) {
			Url(urlProp)
		} else {
			throw ConfigurationException("Configuration is missing 'url' attribute.")
		}

		val hostProp = config.propertyOrNull("host")?.getString()
		host = hostProp ?: "0.0.0.0"

		val portProp = config.propertyOrNull("port")?.getString()?.toInt()
		port = portProp ?: 9782

		val registrationsProp = config.propertyOrNull("registrations")?.getString()
		registrations = if (registrationsProp != null) {
			InstanceRegistrationsType.valueOf(lower(registrationsProp))
		} else {
			InstanceRegistrationsType.Closed
		}

		val identifiersProp = config.propertyOrNull("identifiers")?.getString()
		identifiers = if (identifiersProp != null) {
			IdentifierType.valueOf(lower(identifiersProp))
		} else {
			IdentifierType.Aidx
		}

		database = ConfigurationDatabase()
		queue = ConfigurationQueue()
		timeline = ConfigurationTimeline()
	}
}

class ConfigurationDatabase {
	val host: String
	val port: String
	val db: String
	val user: String
	val password: String

	init {
		val databaseHostProp = config?.propertyOrNull("database.host")?.getString()
		host = databaseHostProp ?: "127.0.0.1"

		val databasePortProp = config?.propertyOrNull("database.port")?.getString()
		port = databasePortProp ?: "5432"

		val databaseDbProp = config?.propertyOrNull("database.db")?.getString()
		db = databaseDbProp ?: throw ConfigurationException("Configuration is missing 'database.db' attribute.")

		val databaseUserProp = config.propertyOrNull("database.user")?.getString()
		user = databaseUserProp ?: throw ConfigurationException("Configuration is missing 'database.user' attribute.")

		val databasePasswordProp = config.propertyOrNull("database.password")?.getString()
		password = databasePasswordProp
			?: throw ConfigurationException("Configuration is missing 'database.password' attribute.")
	}
}

class ConfigurationQueue {
	val inbox: ConfigurationSpecificQueue = ConfigurationSpecificQueue(
		threads = ((config?.propertyOrNull("queue.inbox.threads")?.getString()?.toInt()) ?: 8),
		concurrency = ((config?.propertyOrNull("queue.inbox.concurrency")?.getString()?.toInt()) ?: 8)
	)
	val deliver: ConfigurationSpecificQueue = ConfigurationSpecificQueue(
		threads = ((config?.propertyOrNull("queue.deliver.threads")?.getString()?.toInt()) ?: 6),
		concurrency = ((config?.propertyOrNull("queue.deliver.concurrency")?.getString()?.toInt()) ?: 6)
	)
	val system: ConfigurationSpecificQueue = ConfigurationSpecificQueue(
		threads = ((config?.propertyOrNull("queue.system.threads")?.getString()?.toInt()) ?: 4),
		concurrency = ((config?.propertyOrNull("queue.system.concurrency")?.getString()?.toInt()) ?: 4)
	)
}

data class ConfigurationSpecificQueue(
	val threads: Int,
	val concurrency: Int
)

class ConfigurationTimeline {
	val defaultObjects: Int
	val maxObjects: Int

	val local: ConfigurationSpecificTimeline
	val bubble: ConfigurationSpecificTimeline
	val public: ConfigurationSpecificTimeline

	init {
		val defaultObjectsProp = config?.propertyOrNull("timeline.defaultObjects")?.getString()?.toIntOrNull()
		defaultObjects = defaultObjectsProp ?: 20

		val maxObjectsProp = config?.propertyOrNull("timeline.maxObjects")?.getString()?.toIntOrNull()
		maxObjects = maxObjectsProp ?: 20

		local = ConfigurationSpecificTimeline(
			authRequired = (config?.propertyOrNull("timeline.local.authRequired")?.getString()?.toBooleanStrictOrNull()
				?: false)
		)
		bubble = ConfigurationSpecificTimeline(
			authRequired = (config?.propertyOrNull("timeline.bubble.authRequired")?.getString()?.toBooleanStrictOrNull()
				?: false)
		)
		public = ConfigurationSpecificTimeline(
			authRequired = (config?.propertyOrNull("timeline.public.authRequired")?.getString()?.toBooleanStrictOrNull()
				?: false)
		)
	}
}

data class ConfigurationSpecificTimeline(
	val authRequired: Boolean,
)
