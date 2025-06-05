package site.remlit.blueb.aster.service

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class TimeService {
	fun now(): LocalDateTime {
		return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
	}
}
