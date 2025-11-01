package site.remlit.aster.route.admin

import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.p
import kotlinx.html.styleLink
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.title
import kotlinx.html.tr
import org.jetbrains.exposed.v1.core.neq
import site.remlit.aster.db.table.InviteTable
import site.remlit.aster.route.RouteRegistry
import site.remlit.aster.service.InviteService
import site.remlit.aster.util.webcomponent.adminHeader
import site.remlit.aster.util.webcomponent.adminMain

object AdminInviteRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			get("/admin/invites") {
				val invites = InviteService.getMany(InviteTable.id neq "")
				val inviteCount = InviteService.count(InviteTable.id neq "")

				call.respondHtml(HttpStatusCode.OK) {
					head {
						title { +"Invites" }
						styleLink("/admin/assets/index.css")
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
								+"${invites.size} invites shown, $inviteCount total."
							}
							div {
								button {
									+"Forwards"
								}
								button {
									+"Backwards"
								}
							}
						}
					}
				}
			}
		}
}