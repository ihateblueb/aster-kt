package site.remlit.aster.route.admin

import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.styleLink
import kotlinx.html.title
import site.remlit.aster.route.RouteRegistry
import site.remlit.aster.util.webcomponent.adminHeader
import site.remlit.aster.util.webcomponent.adminMain

object AdminInstanceRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			get("/admin/instances") {
				call.respondHtml(HttpStatusCode.OK) {
					head {
						title { +"Instances" }
						styleLink("/admin/assets/index.css")
					}
					body {
						adminHeader("Instances")
						adminMain {

						}
					}
				}
			}
		}
}