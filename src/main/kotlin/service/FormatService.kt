package site.remlit.blueb.service

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.net.IDN
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class FormatService {
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
