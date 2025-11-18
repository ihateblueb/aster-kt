package site.remlit.aster.service

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import site.remlit.aster.model.Service
import java.net.IDN
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Service for formatting various things.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
object FormatService : Service {
	/**
	 * Determine timezone offset compared to local time.
	 *
	 * @param localDateTime Date and time
	 *
	 * @return Calculated time offset
	 * */
	fun getTimeZoneOffset(localDateTime: LocalDateTime): ZoneOffset {
		return ZoneId.systemDefault().rules.getOffset(localDateTime.toJavaLocalDateTime())
	}

	/**
	 * Formats to the standard ActivityPub date format that can be used in activities.
	 * See: https://www.w3.org/TR/activitystreams-core/#dates
	 *
	 * @param localDateTime Local date and time
	 *
	 * @return Standard date format
	 * */
	fun formatToStandardDateTime(localDateTime: LocalDateTime): String {
		val offsetTime = localDateTime.toJavaLocalDateTime().atOffset(getTimeZoneOffset(localDateTime))
		return offsetTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
	}

	/**
	 * Convert string to ASCII string
	 *
	 * @param string Any string
	 *
	 * @return ASCII string
	 * */
	fun toASCII(string: String): String = IDN.toASCII(string)
}
