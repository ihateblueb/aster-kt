package site.remlit.blueb.aster.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory
import site.remlit.blueb.aster.db.entity.DeliverQueueEntity
import site.remlit.blueb.aster.db.entity.InboxQueueEntity
import site.remlit.blueb.aster.db.table.DeliverQueueTable
import site.remlit.blueb.aster.db.table.InboxQueueTable
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.QueueStatus
import site.remlit.blueb.aster.model.Service
import kotlin.time.Duration.Companion.seconds

class QueueService : Service() {
	companion object {
		private val logger = LoggerFactory.getLogger(this::class.java)

		val inboxScope = CoroutineScope(Dispatchers.Default)
		val deliverScope = CoroutineScope(Dispatchers.Default)

		/**
		 * Initialize queue managers. These check frequently for new items in the queue, and then launch a consumer.
		 * */
		fun init() {
			inboxScope.launch {
				while (true) {
					delay(2.seconds)
					summonInboxConsumersIfNeeded()
				}
			}
			deliverScope.launch {
				while (true) {
					delay(2.seconds)
					summonDeliverConsumersIfNeeded()
				}
			}

			logger.info("Initialized inbox and deliver queues.")
		}

		// queue checkers

		private fun summonInboxConsumersIfNeeded() {
			transaction {
				InboxQueueEntity
					.find { InboxQueueTable.status eq QueueStatus.PENDING }
					.take(Configuration.queue.inbox.concurrency)
					.toList()
					.forEach {
						inboxScope.launch {
							consumeInboxJob(it)
						}
					}
			}
		}

		private fun summonDeliverConsumersIfNeeded() {
			transaction {
				DeliverQueueEntity
					.find { DeliverQueueTable.status eq QueueStatus.PENDING }
					.take(Configuration.queue.deliver.concurrency)
					.toList()
					.forEach {
						deliverScope.launch {
							consumeDeliverJob(it)
						}
					}
			}
		}

		// queue consumers

		private fun consumeInboxJob(job: InboxQueueEntity) {

		}

		private fun consumeDeliverJob(job: DeliverQueueEntity) {

		}
	}
}