package site.remlit.blueb.aster.service

import site.remlit.blueb.aster.model.Service

class ValidationService : Service() {
	companion object {
		fun containsNonAlphanumeric(text: String): Boolean {
			val newText = text.replace(Regex("[^a-zA-Z0-9.]"), "*")
			return newText.contains("*")
		}
	}
}
