package site.remlit.aster.service

import org.apache.commons.text.StringEscapeUtils
import org.owasp.html.PolicyFactory
import org.owasp.html.Sanitizers
import site.remlit.aster.model.Service

/**
 * Service for sanitizing and escaping user submitted content.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
class SanitizerService : Service() {
	companion object {
		private val policy: PolicyFactory = Sanitizers.FORMATTING
			.and(Sanitizers.LINKS)
			.and(Sanitizers.BLOCKS)

		/**
		 * Sanitizes user input
		 *
		 * @param string String to sanitize
		 * @param escape Whether to escape instead of sanitizing
		 * */
		fun sanitize(string: String, escape: Boolean = false): String {
			if (escape)
				return StringEscapeUtils.escapeHtml4(string)

			return policy.sanitize(string)
		}
	}
}
