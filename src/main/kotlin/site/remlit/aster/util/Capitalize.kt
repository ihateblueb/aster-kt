package site.remlit.aster.util

import java.util.Locale.getDefault

/**
 * Capitalize the first character of a string, if needed.
 * */
fun String.capitalize(): String {
	val new = mutableListOf<String>()
	this.split("-").forEach {
		new.add(it.replaceFirstChar { c -> if (c.isLowerCase()) c.titlecase(getDefault()) else c.toString() })
	}
	return new.joinToString("-")
}