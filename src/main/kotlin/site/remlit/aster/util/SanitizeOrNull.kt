package site.remlit.aster.util

import site.remlit.aster.service.SanitizerService

inline fun sanitizeOrNull(string: () -> String?): String? {
	val string = string()
	return if (string != null) SanitizerService.sanitize(string) else null
}