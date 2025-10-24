package site.remlit.aster.util

inline fun minifyDebug(text: () -> String) =
	text().replace("site.remlit.aster", "s.r.b.a")