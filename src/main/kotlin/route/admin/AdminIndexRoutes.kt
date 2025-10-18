package site.remlit.blueb.aster.route.admin

import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.title
import site.remlit.blueb.aster.route.RouteRegistry

object AdminIndexRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			get("/admin") {
				call.response.headers.append("Content-Type", "application/jrd+json")

				call.respondHtml(HttpStatusCode.OK) {
					head {
						title { +"Admin" }
					}
					body {
						h1 { +"Admin" }
					}
				}
			}
		}
}