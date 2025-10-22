package site.remlit.blueb.aster.route.admin

import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.span
import kotlinx.html.styleLink
import kotlinx.html.title
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
				}

				call.respondHtml(HttpStatusCode.OK) {
					head {
						title { +"Queues" }
						styleLink("/admin/assets/index.css")
						// some autoreload script
					}
					body {
						h1 { +"Queues" }
						div {
							this.classes = setOf("ctn")
							span {
								+"$pendingCount jobs pending"
							}
							span {
								+"$completedCount jobs completed"
							}
							span {
								+"$failedCount jobs failed"
							}
						}
					}
				}
			}
		}
}