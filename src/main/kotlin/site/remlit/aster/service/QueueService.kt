package site.remlit.aster.service

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.lessEq
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.core.statements.api.ExposedBlob
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory
import site.remlit.aster.db.entity.DeliverQueueEntity
import site.remlit.aster.db.entity.InboxQueueEntity
import site.remlit.aster.db.entity.UserEntity
import site.remlit.aster.db.table.DeliverQueueTable
import site.remlit.aster.db.table.InboxQueueTable
import site.remlit.aster.model.Configuration
import site.remlit.aster.model.QueueStatus
import site.remlit.aster.model.Service
import site.remlit.aster.registry.InboxHandlerRegistry
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

/**
 * Service for managing persistent queues.
 *
 * @since 2025.10.1.0-SNAPSHOT
 * */
class QueueService : Service() {
	companion object {
		private val logger = LoggerFactory.getLogger(QueueService::class.java)

		val inboxScope = CoroutineScope(Dispatchers.Default + CoroutineName("InboxDispatcher"))
		val deliverScope = CoroutineScope(Dispatchers.Default + CoroutineName("DeliverDispatcher"))

		val activeInboxWorkers: AtomicInteger = AtomicInteger(0)
		val activeDeliverWorkers: AtomicInteger = AtomicInteger(0)

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

		@OptIn(ExperimentalTime::class)
		private fun summonInboxConsumersIfNeeded() {
			transaction {
				InboxQueueEntity
					.find {
						(InboxQueueTable.status eq QueueStatus.PENDING) or
								(InboxQueueTable.status eq QueueStatus.FAILED and (InboxQueueTable.retryAt lessEq TimeService.now()))
					}
					.take(Configuration.queue.inbox.concurrency)
					.toList()
					.forEach {
						if (activeInboxWorkers.get() >= Configuration.queue.inbox.concurrency)
							return@forEach

						activeInboxWorkers.incrementAndGet()
						inboxScope.launch {
							consumeInboxJob(it)
						}
						activeInboxWorkers.decrementAndGet()
					}
			}
		}

		private fun summonDeliverConsumersIfNeeded() {
			transaction {
				DeliverQueueEntity
					.find {
						(DeliverQueueTable.status eq QueueStatus.PENDING) or
								(DeliverQueueTable.status eq QueueStatus.FAILED and (DeliverQueueTable.retryAt lessEq TimeService.now()))
					}
					.take(Configuration.queue.deliver.concurrency)
					.toList()
					.forEach {
						if (activeDeliverWorkers.get() >= Configuration.queue.deliver.concurrency)
							return@forEach

						activeDeliverWorkers.incrementAndGet()
						deliverScope.launch {
							consumeDeliverJob(it)
						}
						activeDeliverWorkers.decrementAndGet()
					}
			}
		}

		// queue consumers

		private fun consumeInboxJob(job: InboxQueueEntity) =
			InboxHandlerRegistry.handle(job)

		private fun consumeDeliverJob(job: DeliverQueueEntity) {}

		// send jobs

		fun insertInboxJob(data: ByteArray, sender: UserEntity?) {
			transaction {
				InboxQueueEntity.new(IdentifierService.generate()) {
					this.status = QueueStatus.PENDING
					this.content = ExposedBlob(data)
					this.sender = sender
				}
			}
		}

		// complete job

		fun completeInboxJob(job: InboxQueueEntity) =
			transaction {
				job.status = QueueStatus.COMPLETED
				job.flush()
			}

		// error job

		@OptIn(ExperimentalTime::class)
		fun errorInboxJob(job: InboxQueueEntity) =
			transaction {
				job.status = QueueStatus.FAILED
				job.retryAt = Clock.System.now().plus((job.retries * 15).minutes)
					.toLocalDateTime(TimeZone.currentSystemDefault())
				job.retries += 1
				job.flush()
			}
	}
}