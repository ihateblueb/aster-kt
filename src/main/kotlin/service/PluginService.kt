package site.remlit.blueb.aster.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import site.remlit.blueb.aster.model.Service

class PluginService : Service() {
	companion object {
		private val logger: Logger = LoggerFactory.getLogger(this::class.java)

		val pluginManager = site.remlit.blueb.aster.util.pluginManager

		fun initialize() {
			pluginManager.loadPlugins()

			logger.info("${pluginManager.plugins.count()} plugins loaded")
			pluginManager.plugins.forEach { plugin ->
				logger.info("Loaded plugin ${plugin.pluginId}")
			}

			pluginManager.startPlugins()
		}
	}
}
