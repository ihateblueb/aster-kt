package site.remlit.blueb.aster.plugin

import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import site.remlit.blueb.aster.model.plugin.PluginManifest

object PluginRegistry {
	private val logger: Logger = LoggerFactory.getLogger(this::class.java)

	/**
	 * List of currently enabled plugins
	 * */
	val plugins: MutableList<Pair<PluginManifest, AsterPlugin>> = emptyList<Pair<PluginManifest, AsterPlugin>>().toMutableList()

	/**
	 * Adds plugin to registry and runs it's enable hook.
	 *
	 * @param plugin Plugin to enable
	 * */
	fun enablePlugin(manifest: PluginManifest, plugin: AsterPlugin) {
		if (
			plugins.find { it.first == manifest } != null ||
			plugins.find { it.second == plugin } != null
		) throw IllegalStateException("Attempted to register duplicate plugin")

		plugins.add(manifest to plugin)
		plugin.enable()

		logger.info("Enabled plugin ${manifest.name} by ${manifest.authors.joinToString(", ")}")
	}

	/**
	 * Removes plugin to registry and runs it's disable hook.
	 *
	 * @param plugin Plugin to disable
	 * */
	fun disablePlugin(plugin: AsterPlugin) {
		val pair = plugins.find { it.second == plugin }
		if (pair == null) return

		plugin.disable()
		plugins.remove(pair)

		logger.info("Disabled plugin ${pair.first}")
	}

	/**
	 * Disables all currently active plugins
	 * */
	fun disableAll() {
		runBlocking {
			try {
				for (plugin in plugins) {
					disablePlugin(plugin.second)
				}
			} catch (_: Exception) {
			}
		}
	}
}