package site.remlit.aster.route.admin

import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.main
import kotlinx.html.styleLink
import kotlinx.html.title
import site.remlit.aster.route.RouteRegistry
import site.remlit.aster.util.webcomponent.adminHeader

object AdminIndexRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			staticResources("/admin/assets", "admin")

			get("/admin") {
				call.respondHtml(HttpStatusCode.OK) {
					head {
						title { +"Admin Panel" }
						styleLink("/admin/assets/index.css")
						// some autoreload script
					}
					body {
						adminHeader("Overview")
						main {

						}
					}
				}
			}
		}
}