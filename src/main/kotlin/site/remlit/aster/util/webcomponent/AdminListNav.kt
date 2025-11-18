package site.remlit.aster.util.webcomponent

import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.classes
import kotlinx.html.div

fun FlowContent.adminListNav(base: Long, take: Int) {
	div {
		classes = setOf("navBtns")
		a {
			href = "?offset=${base - take}"
			+"Backwards"
		}
		a {
			href = "?offset=${base + take}"
			+"Forwards"
		}
	}
}
