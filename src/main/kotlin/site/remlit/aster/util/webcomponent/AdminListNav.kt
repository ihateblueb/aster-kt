package site.remlit.aster.util.webcomponent

import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.onClick

fun FlowContent.adminListNav(next: String) {
	div {
		classes = setOf("navBtns")
		button {
			onClick = "navBackwards()"
			+"Backwards"
		}
		a {
			href = "?since=$next"
			+"Forwards"
		}
	}
}