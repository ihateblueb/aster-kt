package site.remlit.blueb.aster.service

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import site.remlit.blueb.aster.model.Service

class TimeService : Service() {
	companion object {
		/**
		 * Gets current LocalDateTime
		 *
		 * @return Current LocalDateTime
		 * */
		fun now(): LocalDateTime {
			return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
		}
	}
}
