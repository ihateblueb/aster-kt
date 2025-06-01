package site.remlit.blueb.service

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class TimelineService {
	fun normalizeTake(take: Int?): Int {
		if (take != null) {
			if (take > 45) {
				return 45
			} else if (take < 1) {
				return 1
			}
			return take
		} else {
			return 15
		}
	}

	fun normalizeSince(since: String?): LocalDateTime {
		val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()
		return LocalDateTime.parse(since ?: now)
	}
}
