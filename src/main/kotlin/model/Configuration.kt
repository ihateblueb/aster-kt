package site.remlit.blueb.model

import io.ktor.http.*
import io.ktor.server.config.yaml.*
import java.io.File
import java.util.Locale.getDefault

var workingDir = File(".").absolutePath.toString().removeSuffix(".")
var configPath = workingDir + "configuration.yaml"

val config = YamlConfig(configPath)

class Configuration {
	val name: String

	val url: Url
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
			InstanceRegistrationsType.valueOf(lower(registrationsProp))
		} else {
			InstanceRegistrationsType.Closed
		}

		var identifiersProp = config?.propertyOrNull("identifiers")?.getString()
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

class ConfigurationQueue {
	val inbox: ConfigurationSpecificQueue
	val deliver: ConfigurationSpecificQueue
	val system: ConfigurationSpecificQueue

	init {
		inbox = ConfigurationSpecificQueue(
			threads = ((config?.propertyOrNull("queue.inbox.threads")?.getString()?.toInt()) ?: 8),
			concurrency = ((config?.propertyOrNull("queue.inbox.concurrency")?.getString()?.toInt()) ?: 8)
		)
		deliver = ConfigurationSpecificQueue(
			threads = ((config?.propertyOrNull("queue.deliver.threads")?.getString()?.toInt()) ?: 6),
			concurrency = ((config?.propertyOrNull("queue.deliver.concurrency")?.getString()?.toInt()) ?: 6)
		)
		system = ConfigurationSpecificQueue(
			threads = ((config?.propertyOrNull("queue.system.threads")?.getString()?.toInt()) ?: 4),
			concurrency = ((config?.propertyOrNull("queue.system.concurrency")?.getString()?.toInt()) ?: 4)
		)
	}
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
		var defaultObjectsProp = config?.propertyOrNull("timeline.defaultObjects")?.getString()?.toIntOrNull()
		defaultObjects = defaultObjectsProp ?: 20

		var maxObjectsProp = config?.propertyOrNull("timeline.maxObjects")?.getString()?.toIntOrNull()
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
