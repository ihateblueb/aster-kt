package site.remlit.blueb.aster.util

import site.remlit.blueb.aster.service.SanitizerService

inline fun sanitizeOrNull(string: () -> String?): String? {
	val string = string()
	return if (string != null) SanitizerService.sanitize(string) else null
}