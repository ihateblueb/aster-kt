package site.remlit.blueb.aster.model

import io.ktor.http.*
import io.ktor.server.config.yaml.*
import site.remlit.blueb.aster.common.model.InstanceRegistrationsType
import site.remlit.blueb.aster.util.capitalize
import java.io.File
import kotlin.concurrent.thread

var workingDir = File(".").absolutePath.toString().removeSuffix(".")
var configPath = workingDir + "configuration.yaml"
var config = YamlConfig(configPath)

object Configuration {
	val name: String get() = config?.propertyOrNull("name")?.getString() ?: "Aster"

	val url: Url
		get() =
			Url(
				config?.propertyOrNull("url")?.getString()
					?: throw Exception("Configuration is missing 'url' attribute.")
			)

	val port: Int get() = config?.propertyOrNull("port")?.getString()?.toInt() ?: 9782
	val host: String get() = config?.propertyOrNull("host")?.getString() ?: "0.0.0.0"

	val debug: Boolean get() = config?.propertyOrNull("debug")?.getString()?.toBoolean() ?: false
	val builtinFrontend: Boolean get() = config?.propertyOrNull("builtinFrontend")?.getString()?.toBoolean() ?: true
	val hideRemoteContent: Boolean get() = config?.propertyOrNull("hideRemoteContent")?.getString()?.toBoolean() ?: true

	val registrations: InstanceRegistrationsType
		get() =
			InstanceRegistrationsType.valueOf(
				config?.propertyOrNull("registrations")?.getString()?.capitalize() ?: "Closed"
			)
	val identifiers: IdentifierType
		get() =
			IdentifierType.valueOf(config?.propertyOrNull("identifiers")?.getString()?.capitalize() ?: "Aidx")

	val database: ConfigurationDatabase get() = ConfigurationDatabase()
	val queue: ConfigurationQueue get() = ConfigurationQueue()
	val timeline: ConfigurationTimeline get() = ConfigurationTimeline()

	init {
		thread(name = "Configuration Refresher") {
			while (true) {
				Thread.sleep(30 * 1000L)
				config = YamlConfig(configPath)
			}
		}
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
