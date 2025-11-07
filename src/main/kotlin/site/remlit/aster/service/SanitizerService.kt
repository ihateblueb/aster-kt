package site.remlit.aster.service

import org.owasp.html.HtmlPolicyBuilder
import org.owasp.html.PolicyFactory
import site.remlit.aster.model.Service

/**
 * Service for sanitizing and escaping user submitted content.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
object SanitizerService : Service {
	private val policy: PolicyFactory = HtmlPolicyBuilder()
		.allowElements("p", "a")
		.disallowAttributes("class", "id", "onclick", "style")
		.onElements("p", "a")
		.toFactory()

	/**
	 * Sanitizes user input
	 *
	 * @param string String to sanitize
	 * @param escape Whether to escape instead of sanitizing
	 *
	 * @return Sanitized string
	 * */
	// todo: escaping
	fun sanitize(string: String, escape: Boolean = false): String =
		policy.sanitize(string)
}
