package site.remlit.blueb.aster.service

import site.remlit.blueb.aster.model.Service

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
