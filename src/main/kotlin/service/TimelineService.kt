package me.blueb.service

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
}
