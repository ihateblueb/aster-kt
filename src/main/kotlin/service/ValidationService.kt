package site.remlit.blueb.service

class ValidationService {
	fun containsNonAlphanumeric(text: String): Boolean {
		val newText = text.replace(Regex("[^a-zA-Z0-9.]"), "*")
		return newText.contains("*")
	}
}
