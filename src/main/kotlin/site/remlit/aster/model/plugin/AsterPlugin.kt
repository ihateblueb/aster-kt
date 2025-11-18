package site.remlit.aster.model.plugin

import org.slf4j.Logger

/**
 * Interface used to create a plugin for Aster.
 * */
interface AsterPlugin {
	val logger: Logger

	fun enable()
	fun disable()
}
