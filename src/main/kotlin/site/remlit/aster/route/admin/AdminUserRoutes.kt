package site.remlit.aster.route.admin

import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.head
import kotlinx.html.p
import kotlinx.html.script
import kotlinx.html.styleLink
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.title
import kotlinx.html.tr
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.core.neq
import site.remlit.aster.common.model.type.RoleType
import site.remlit.aster.db.table.UserTable
import site.remlit.aster.route.RouteRegistry
import site.remlit.aster.service.RoleService
import site.remlit.aster.service.TimelineService
import site.remlit.aster.service.UserService
import site.remlit.aster.util.webcomponent.adminHeader
import site.remlit.aster.util.webcomponent.adminListNav
import site.remlit.aster.util.webcomponent.adminMain

object AdminUserRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			get("/admin/users") {
				val since = TimelineService.normalizeSince(call.parameters["since"])
				val isLocal = true

				val users = UserService.getMany(
					(if (isLocal) UserTable.host eq null else UserTable.id neq null)
							and (UserTable.createdAt less since)
				)

				val totalUsers = UserService.count(
					(if (isLocal) UserTable.host eq null else UserTable.id neq null)
				)

				call.respondHtml(HttpStatusCode.OK) {
					head {
						title { +"Users" }
						styleLink("/admin/assets/index.css")
						script { src = "/admin/assets/index.js" }
					}
					body {
						adminHeader("Users")
						adminMain {
							table {
								tr {
									classes = setOf("header")
									th { +"Username" }
									th { +"Status" }
									th { +"Actions" }
								}
								for (user in users) {
									tr {
										td {
											+"@${user.username}${if (user.host != null) "@" + user.host else ""}"
										}
										td {
											val status = mutableListOf<String>()

											if (user.activated) status += "Activated" else status += "Unactivated"

											val highestRole = RoleService.getUserHighestRole(user.id.toString())
											if (highestRole == RoleType.Admin) status += "Admin"
											if (highestRole == RoleType.Mod) status += "Mod"

											if (user.suspended) status += "Suspended"
											if (user.sensitive) status += "Sensitive"

											+status.joinToString(separator = ", ")
										}
										td {
											button {
												+"Activate"
											}
											button {
												+"Suspend"
											}
										}
									}
								}
							}
							p {
								+"${users.size}${if (isLocal) " local" else ""} users shown, $totalUsers total."
							}
							adminListNav(users.last().createdAt.toString())
						}
					}
				}
			}
		}
}