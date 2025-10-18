package site.remlit.blueb.aster.util.log

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.pattern.color.ANSIConstants
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase

class HighlightConversion : ForegroundCompositeConverterBase<ILoggingEvent>() {
	override fun getForegroundColorCode(event: ILoggingEvent?): String =
		when (event?.level) {
			Level.ERROR -> ANSIConstants.RED_FG
			Level.WARN -> ANSIConstants.YELLOW_FG
			Level.INFO -> ANSIConstants.BLUE_FG
			Level.DEBUG -> ANSIConstants.CYAN_FG
			else -> ANSIConstants.DEFAULT_FG
		}
}