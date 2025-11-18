package site.remlit.aster.util.webcomponent

import kotlinx.html.FlowContent
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.main

fun FlowContent.adminMain(content: () -> Unit) {
	main {
		div {
			classes = setOf("inner")
			content()
		}
	}
}
