package site.remlit.aster.service

import site.remlit.aster.model.Service

/**
 * Service for validating user submitted content.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
object ValidationService : Service {
	/**
	 * Determines if a string contains non-alphanumeric characters.
	 *
	 * @param text String to check
	 *
	 * @return Whether a string contains non-alphanumeric characters
	 * */
	@JvmStatic
	fun containsNonAlphanumeric(text: String): Boolean {
		val newText = text.replace(Regex("[^a-zA-Z0-9.]"), "*")
		return newText.contains("*")
	}
}
