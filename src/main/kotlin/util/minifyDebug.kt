package site.remlit.blueb.aster.util

inline fun minifyDebug(text: () -> String) =
	text().replace("site.remlit.blueb.aster", "s.r.b.a")