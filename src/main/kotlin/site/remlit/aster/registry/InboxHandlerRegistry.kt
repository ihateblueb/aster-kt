package site.remlit.aster.registry

import kotlinx.coroutines.runBlocking
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory
import site.remlit.aster.db.entity.InboxQueueEntity
import site.remlit.aster.model.Configuration
import site.remlit.aster.model.ap.ApInboxHandler
import site.remlit.aster.model.ap.ApTypedObject
import site.remlit.aster.service.QueueService
import site.remlit.aster.service.ap.inbox.ApBiteHandler
import site.remlit.aster.service.ap.inbox.ApCreateHandler
import site.remlit.aster.util.jsonConfig

object InboxHandlerRegistry {
	private val logger = LoggerFactory.getLogger(InboxHandlerRegistry::class.java)

	val inboxHandlers = mutableListOf<Pair<String, ApInboxHandler>>()

	fun handle(job: InboxQueueEntity) {
		val typedObject = jsonConfig.decodeFromString<ApTypedObject>(String(job.content.bytes))

		if (Configuration.debug)
			logger.debug(
				"[{}] Consuming object of type {} from {} on attempt {}",
				job.id,
				typedObject.type,
				transaction { job.sender?.apId ?: "unknown" },
				job.retries + 1
			)

		runBlocking {
			try {
				for (handler in inboxHandlers) {
					if (handler.first == typedObject.type)
						handler.second.handle(job)
				}
				QueueService.completeInboxJob(job)
			} catch (e: Exception) {
				logger.error("Job ${job.id} failed: ${e.message?.replace("\n", "")}")
				QueueService.errorInboxJob(job)
			}
		}
	}

	fun register(type: String, handler: ApInboxHandler) {
		inboxHandlers.add(Pair(type, handler))
	}

	@ApiStatus.Internal
	fun registerDefaults() {
		register("Bite", ApBiteHandler())
		register("Create", ApCreateHandler())
	}
}