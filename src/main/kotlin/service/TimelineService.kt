package site.remlit.blueb.aster.service

import kotlinx.datetime.LocalDateTime
import site.remlit.blueb.aster.model.Service

class TimelineService : Service() {
	companion object {
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
			val now = TimeService.now().toString()
			return LocalDateTime.parse(since ?: now)
		}
	}
}
