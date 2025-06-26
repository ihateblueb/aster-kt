package site.remlit.blueb.aster.service

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import site.remlit.blueb.aster.model.Service
import java.net.IDN
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class FormatService : Service() {
	companion object {
		fun getTimeZoneOffset(localDateTime: LocalDateTime): ZoneOffset {
			return ZoneId.systemDefault().rules.getOffset(localDateTime.toJavaLocalDateTime())
		}

		// See: https://www.w3.org/TR/activitystreams-core/#dates
		fun formatToStandardDateTime(localDateTime: LocalDateTime): String {
			val offsetTime = localDateTime.toJavaLocalDateTime().atOffset(getTimeZoneOffset(localDateTime))
			return offsetTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
		}

		fun toASCII(string: String) = IDN.toASCII(string)
	}
}
