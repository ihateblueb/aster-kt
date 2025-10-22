package site.remlit.blueb.aster.route.admin

import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.head
import kotlinx.html.li
import kotlinx.html.span
import kotlinx.html.styleLink
import kotlinx.html.title
import kotlinx.html.ul
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import site.remlit.blueb.aster.db.entity.InboxQueueEntity
import site.remlit.blueb.aster.db.table.InboxQueueTable
import site.remlit.blueb.aster.model.QueueStatus
import site.remlit.blueb.aster.route.RouteRegistry

object AdminQueueRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			get("/admin/queues") {
				var pendingCount: Long = 0
				var completedCount: Long = 0
				var failedCount: Long = 0

				val pendingHosts: MutableList<String> = mutableListOf()
				val completedHosts: MutableList<String> = mutableListOf()
				val failedHosts: MutableList<String> = mutableListOf()

				transaction {
					pendingCount = InboxQueueEntity
						.find { InboxQueueTable.status eq QueueStatus.PENDING }
						.count()
					completedCount = InboxQueueEntity
						.find { InboxQueueTable.status eq QueueStatus.COMPLETED }
						.count()
					failedCount = InboxQueueEntity
						.find { InboxQueueTable.status eq QueueStatus.FAILED }
						.count()

					InboxQueueEntity
						.find { InboxQueueTable.status eq QueueStatus.PENDING }
						.forEach { e ->
							pendingHosts.add(e.sender?.host ?: return@forEach)
						}
					InboxQueueEntity
						.find { InboxQueueTable.status eq QueueStatus.COMPLETED }
						.forEach { e ->
							completedHosts.add(e.sender?.host ?: return@forEach)
						}
					InboxQueueEntity
						.find { InboxQueueTable.status eq QueueStatus.FAILED }
						.forEach { e ->
							failedHosts.add(e.sender?.host ?: return@forEach)
						}
				}

				call.respondHtml(HttpStatusCode.OK) {
					head {
						title { +"Queues" }
						styleLink("/admin/assets/index.css")
						// some autoreload script
					}
					body {
						h1 { +"Queues" }
						h2 { +"Inbox" }
						div {
							this.classes = setOf("ctn")
							div {
								this.classes = setOf("ctn", "column")
								span {
									+"$pendingCount jobs pending"
								}
								ul {
									for (host in pendingHosts) {
										li {
											+host
										}
									}
								}
							}
							div {
								this.classes = setOf("ctn", "column")
								span {
									+"$completedCount jobs completed"
								}
								ul {
									for (host in completedHosts) {
										li {
											+host
										}
									}
								}
							}
							div {
								this.classes = setOf("ctn", "column")
								span {
									+"$failedCount jobs failed"
								}
								ul {
									for (host in failedHosts) {
										li {
											+host
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