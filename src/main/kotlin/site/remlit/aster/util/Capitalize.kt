package site.remlit.aster.util

import java.util.Locale.getDefault

/**
 * Capitalize the first character of a string, if needed.
 * */
fun String.capitalize() =
	replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() }