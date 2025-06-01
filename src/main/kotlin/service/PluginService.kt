package site.remlit.blueb.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PluginService {
	private val logger: Logger = LoggerFactory.getLogger(this::class.java)

	val pluginManager = site.remlit.blueb.util.pluginManager

	fun initialize() {
		pluginManager.loadPlugins()

		logger.info("${pluginManager.plugins.count()} plugins loaded")
		pluginManager.plugins.forEach { plugin ->
			logger.info("Loaded plugin ${plugin.pluginId}")
		}

		pluginManager.startPlugins()
	}
}
