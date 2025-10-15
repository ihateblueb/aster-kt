package site.remlit.blueb.aster.service

import site.remlit.blueb.aster.model.Service

/**
 * Service for validating user submitted content.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
class ValidationService : Service() {
	companion object {
		/**
		 * Determines if a string contains non-alphanumeric characters.
		 *
		 * @param text String to check
		 *
		 * @return Whether a string contains non-alphanumeric characters
		 * */
		fun containsNonAlphanumeric(text: String): Boolean {
			val newText = text.replace(Regex("[^a-zA-Z0-9.]"), "*")
			return newText.contains("*")
		}
	}
}
