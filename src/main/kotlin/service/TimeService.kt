package site.remlit.blueb.aster.service

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import site.remlit.blueb.aster.model.Service
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Service for time related utilities.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
class TimeService : Service() {
	companion object {
		/**
		 * Gets current LocalDateTime
		 *
		 * @return Current LocalDateTime
		 * */
		@OptIn(ExperimentalTime::class)
		fun now(): LocalDateTime {
			return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
		}
	}
}
