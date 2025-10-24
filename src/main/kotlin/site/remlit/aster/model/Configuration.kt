package site.remlit.aster.model

import io.ktor.http.*
import io.ktor.server.config.yaml.*
import site.remlit.aster.common.model.type.FileStorageType
import site.remlit.aster.common.model.type.InstanceRegistrationsType
import site.remlit.aster.util.capitalize
import java.io.File
import java.nio.file.Path
import kotlin.concurrent.thread
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

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

	val registrations: InstanceRegistrationsType
		get() =
			InstanceRegistrationsType.valueOf(
				config?.propertyOrNull("registrations")?.getString()?.capitalize() ?: "Closed"
			)
	val identifiers: IdentifierType
		get() =
			IdentifierType.valueOf(config?.propertyOrNull("identifiers")?.getString()?.capitalize() ?: "Aidx")

	val database: ConfigurationDatabase = ConfigurationDatabase()
	val queue: ConfigurationQueue = ConfigurationQueue()
	val timeline: ConfigurationTimeline = ConfigurationTimeline()
	val fileStorage: ConfigurationFileStorage = ConfigurationFileStorage()

	val hideRemoteContent: Boolean get() = config?.propertyOrNull("hideRemoteContent")?.getString()?.toBoolean() ?: true

	init {
		thread(name = "Configuration Refresher") {
			while (true) {
				Thread.sleep(30 * 1000L)
				config = YamlConfig(configPath)
			}
		}
	}
}

class ConfigurationFileStorage {
	val type: FileStorageType
		get() = FileStorageType.valueOf(
			config?.propertyOrNull("fileStorage.type")?.getString()?.capitalize() ?: "Local"
		)
	val localPath: Path
		get() {
			val path = Path(config?.propertyOrNull("fileStorage.localPath")?.getString() ?: "/var/lib/aster/files")

			if (!path.exists())
				throw Exception("File storage path doesn't exist")
			if (!path.isDirectory())
				throw Exception("File storage path is a file, not a directory")
			if (!path.toFile().canWrite())
				throw Exception("File storage path is not writeable")

			return path
		}

	val maxUploadSize: Int
		get() = config?.propertyOrNull("fileStorage.maxUploadSize")?.getString()?.toInt() ?: 25
}

class ConfigurationDatabase {
	val host: String get() = config?.propertyOrNull("database.host")?.getString() ?: "127.0.0.1"
	val port: String get() = config?.propertyOrNull("database.port")?.getString() ?: "5432"
	val db: String
		get() = config?.propertyOrNull("database.db")?.getString()
			?: throw Exception("Configuration is missing 'database.db' attribute.")
	val user: String
		get() = config?.propertyOrNull("database.user")?.getString()
			?: throw Exception("Configuration is missing 'database.user' attribute.")
	val password: String
		get() = config?.propertyOrNull("database.password")?.getString()
			?: throw Exception("Configuration is missing 'database.password' attribute.")
}

class ConfigurationQueue {
	val inbox: ConfigurationSpecificQueue
		get() = ConfigurationSpecificQueue(
			(config?.propertyOrNull("queue.inbox.concurrency")?.getString()?.toInt() ?: 8)
		)
	val deliver: ConfigurationSpecificQueue
		get() = ConfigurationSpecificQueue(
			((config?.propertyOrNull("queue.deliver.concurrency")?.getString()?.toInt()) ?: 6)
		)
	val system: ConfigurationSpecificQueue
		get() = ConfigurationSpecificQueue(
			((config?.propertyOrNull("queue.system.concurrency")?.getString()?.toInt()) ?: 4)
		)
}

data class ConfigurationSpecificQueue(
	val concurrency: Int
)

class ConfigurationTimeline {
	val defaultObjects: Int get() = config?.propertyOrNull("timeline.defaultObjects")?.getString()?.toIntOrNull() ?: 15
	val maxObjects: Int get() = config?.propertyOrNull("timeline.maxObjects")?.getString()?.toIntOrNull() ?: 35

	val local: ConfigurationSpecificTimeline
		get() = ConfigurationSpecificTimeline(
			authRequired = (config?.propertyOrNull("timeline.local.authRequired")?.getString()?.toBooleanStrictOrNull()
				?: false)
		)
	val bubble: ConfigurationBubbleTimeline
		get() = ConfigurationBubbleTimeline(
			authRequired = (config?.propertyOrNull("timeline.bubble.authRequired")?.getString()?.toBooleanStrictOrNull()
				?: false),
			hosts = (config?.propertyOrNull("timeline.bubble.hosts")?.getList()
				?: emptyList())
		)
	val public: ConfigurationSpecificTimeline
		get() = ConfigurationSpecificTimeline(
			authRequired = (config?.propertyOrNull("timeline.public.authRequired")?.getString()?.toBooleanStrictOrNull()
				?: false)
		)
}

data class ConfigurationSpecificTimeline(
	val authRequired: Boolean,
)

data class ConfigurationBubbleTimeline(
	val authRequired: Boolean,
	val hosts: List<String>,
)
