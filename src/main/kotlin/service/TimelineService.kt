package site.remlit.blueb.aster.service

import kotlinx.datetime.LocalDateTime
import site.remlit.blueb.aster.model.Service

/**
 * Service for timeline related utilities.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
class TimelineService : Service() {
	companion object {
		/**
		 * Ensures the timeline take is within the acceptable range
		 *
		 * @param take Number provided by a user
		 *
		 * @return Number acceptable for the server to use
		 * */
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

		/**
		 * Normalizes since date and time provided by a user
		 *
		 * @param since Time date string provided by a user
		 *
		 * @return Time and date acceptable for the server to use
		 * */
		fun normalizeSince(since: String?): LocalDateTime {
			val now = TimeService.now().toString()
			return LocalDateTime.parse(since ?: now)
		}
	}
}
