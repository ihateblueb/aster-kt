package site.remlit.blueb.aster.service

import org.apache.commons.text.StringEscapeUtils
import org.owasp.html.PolicyFactory
import org.owasp.html.Sanitizers
import site.remlit.blueb.aster.model.Service

class SanitizerService : Service() {
	companion object {
		private val policy: PolicyFactory = Sanitizers.FORMATTING
			.and(Sanitizers.LINKS)
			.and(Sanitizers.BLOCKS)

		fun sanitize(string: String, escape: Boolean = false): String {
			if (escape)
				return StringEscapeUtils.escapeHtml4(string)

			return policy.sanitize(string)
		}
	}
}
