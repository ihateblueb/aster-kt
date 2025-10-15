package site.remlit.blueb.aster.service

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import site.remlit.blueb.aster.model.Service
import site.remlit.blueb.aster.model.plugin.PluginManifest
import site.remlit.blueb.aster.plugin.AsterPlugin
import site.remlit.blueb.aster.plugin.PluginRegistry
import java.io.InputStream
import java.net.URI
import java.net.URLClassLoader
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name

/**
 * Service for managing plugins.
 *
 * @since 2025.9.1.1-SNAPSHOT
 * */
class PluginService : Service() {
	companion object {
		private val logger: Logger = LoggerFactory.getLogger(this::class.java)

		private val pluginDir = Path("plugins")

		/**
		 * Find and enable plugins in plugins directory.
		 * */
		@OptIn(ExperimentalSerializationApi::class)
		fun initialize() {
			if (!pluginDir.exists()) pluginDir.createDirectories()

			pluginDir.listDirectoryEntries()
				.filter { !it.endsWith(".jar") }
				.forEach { jar ->
					ZipFile(jar.toFile()).use { zip ->
						val pluginManifest = zip.getEntry("plugin.json")
						if (pluginManifest == null) {
							logger.warn("Plugin manifest missing for ${jar.name}, skipping")
							return@use
						}

						fun getInputStream(entry: ZipEntry): InputStream = zip.getInputStream(entry)

						getInputStream(pluginManifest).use { manifestStream ->
							val manifest = Json.decodeFromStream<PluginManifest>(manifestStream)

							val classLoader = URLClassLoader(
								arrayOf(URI("file://${jar.absolutePathString()}").toURL()),
								this::class.java.classLoader
							)

							logger.debug("Current URLClassLoader URLS: {}", classLoader.urLs)

							try {
								val mainClass = classLoader.loadClass(manifest.mainClass)
								PluginRegistry.enablePlugin(
									manifest,
									mainClass.getDeclaredConstructor().newInstance() as AsterPlugin
								)
							} catch (e: Exception) {
								logger.error("Failed to load plugin ${manifest.name}! ${e.message}", e)
							}
						}
					}
				}
		}
	}
}
