package site.remlit.aster.plugin

import org.slf4j.Logger

/**
 * Interface used to create a plugin for Aster.
 * */
interface AsterPlugin {
	val logger: Logger

	fun enable()
	fun disable()
}