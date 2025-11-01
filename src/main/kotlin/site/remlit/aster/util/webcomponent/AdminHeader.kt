package site.remlit.aster.util.webcomponent

import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.header
import kotlinx.html.span

fun FlowContent.adminHeader(currentPage: String) {
	fun FlowContent.adminHeaderButton(page: String, to: String) {
		a {
			classes = if (currentPage == page) setOf("selected") else setOf()
			href = to
			+page
		}
	}

	header {
		div {
			classes = setOf("inner")
			span { +"Admin Panel" }
			adminHeaderButton("Overview", "/admin")
			adminHeaderButton("Users", "/admin/users")
			adminHeaderButton("Instances", "/admin/instances")
			adminHeaderButton("Queues", "/admin/queues")
			adminHeaderButton("Plugins", "/admin/plugins")
		}
	}
}