package me.blueb.service

import org.apache.commons.text.StringEscapeUtils

class SanitizerService {
	fun sanitize(string: String, escape: Boolean = false): String {
		if (escape)
			return StringEscapeUtils.escapeHtml4(string)

		// todo: https://github.com/OWASP/java-html-sanitizer
		return string
	}
}
