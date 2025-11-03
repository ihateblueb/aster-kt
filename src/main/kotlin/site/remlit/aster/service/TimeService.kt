package site.remlit.aster.service

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import site.remlit.aster.model.Service
import java.time.temporal.ChronoUnit
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

		/**
		 * Creates a LocalDateTime a specified amount of hours in the past.
		 *
		 * @param hours Number of hours in the past
		 *
		 * @return Created LocalDateTime
		 * */
		fun hoursAgo(hours: Long): LocalDateTime {
			return now()
				.toJavaLocalDateTime().minus(hours, ChronoUnit.HOURS).toKotlinLocalDateTime()
		}

		/**
		 * Creates a LocalDateTime a specified amount of days in the past.
		 *
		 * @param days Number of days in the past
		 *
		 * @return Created LocalDateTime
		 * */
		fun daysAgo(days: Long): LocalDateTime = hoursAgo(days * 24)
	}
}
