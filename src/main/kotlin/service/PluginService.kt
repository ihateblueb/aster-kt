package site.remlit.blueb.aster.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import site.remlit.blueb.aster.model.Service

class PluginService : Service() {
	companion object {
		private val logger: Logger = LoggerFactory.getLogger(this::class.java)

		fun initialize() {
			logger.info("Unimplemented")
		}
	}
}
