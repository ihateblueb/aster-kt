package me.blueb.service

import org.pf4j.DefaultPluginManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PluginService {
	private val logger: Logger = LoggerFactory.getLogger(this::class.java)

	val pluginManager = DefaultPluginManager()

	fun initialize() {
		pluginManager.plugins.forEach { plugin ->
			logger.info("Loaded plugin ${plugin.pluginId}")
		}
	}
}
