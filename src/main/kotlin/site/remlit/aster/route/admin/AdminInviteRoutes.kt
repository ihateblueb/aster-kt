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
import org.jetbrains.exposed.v1.core.neq
import site.remlit.aster.db.table.InviteTable
import site.remlit.aster.model.Configuration
import site.remlit.aster.route.RouteRegistry
import site.remlit.aster.service.InviteService
import site.remlit.aster.util.webcomponent.adminHeader
import site.remlit.aster.util.webcomponent.adminListNav
import site.remlit.aster.util.webcomponent.adminMain

object AdminInviteRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			get("/admin/invites") {
				val take = Configuration.timeline.defaultObjects
				val offset = call.parameters["offset"]?.toLong() ?: 0

				val invites = InviteService.getMany(
					InviteTable.id neq "",
					take,
					offset
				)
				val totalInvites = InviteService.count(
					InviteTable.id neq ""
				)

				call.respondHtml(HttpStatusCode.OK) {
					head {
						title { +"Invites" }
						styleLink("/admin/assets/index.css")
						script { src = "/admin/assets/index.js" }
					}
					body {
						adminHeader("Invites")
						adminMain {
							table {
								tr {
									classes = setOf("header")
									th { +"Invite" }
									th { +"Actions" }
								}
								for (invite in invites) {
									tr {
										td {
											classes = setOf("_75")
											+invite.code
										}
										td {
											button {
												+"Delete"
											}
										}
									}
								}
							}
							p {
								+"${invites.size} invites shown, $totalInvites total."
							}
							adminListNav(offset, take)
						}
					}
				}
			}
		}
}