package site.remlit.aster.route.admin

import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.InputType
import kotlinx.html.b
import kotlinx.html.body
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.hidden
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.li
import kotlinx.html.p
import kotlinx.html.styleLink
import kotlinx.html.title
import kotlinx.html.ul
import site.remlit.aster.plugin.PluginRegistry
import site.remlit.aster.route.RouteRegistry

object AdminPluginRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			post("/admin/plugins") {
				val string = call.receive<String>()
				call.respondText { "$string" }
			}

			get("/admin/plugins") {
				call.respondHtml(HttpStatusCode.OK) {
					head {
						title { +"Plugins" }
						styleLink("/admin/assets/index.css")
					}
					body {
						h1 { +"Plugins" }
						form {
							input {
								type = InputType.text
								id = "action"
								value = "reload"
								hidden = true
							}
							input {
								type = InputType.submit
								value = "Reload"
							}
						}
						div {
							this.classes = setOf("ctn")
							div {
								this.classes = setOf("ctn", "column")
								ul {
									for (plugin in PluginRegistry.plugins) {
										li {
											b { +"${plugin.first.name} ${plugin.first.version}" }
											p { +plugin.first.mainClass }
										}
									}
								}
							}
						}
					}
				}
			}
		}
}