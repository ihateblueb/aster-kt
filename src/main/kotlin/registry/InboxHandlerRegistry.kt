package site.remlit.blueb.aster.registry

import kotlinx.coroutines.runBlocking
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory
import site.remlit.blueb.aster.db.entity.InboxQueueEntity
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.ap.ApInboxHandler
import site.remlit.blueb.aster.model.ap.ApTypedObject
import site.remlit.blueb.aster.service.QueueService
import site.remlit.blueb.aster.service.ap.inbox.ApBiteHandler
import site.remlit.blueb.aster.util.jsonConfig

object InboxHandlerRegistry {
    private val logger = LoggerFactory.getLogger(InboxHandlerRegistry::class.java)

    val inboxHandlers = listOf<Pair<String, ApInboxHandler>>()

    fun register(type: String, handler: ApInboxHandler) {
        inboxHandlers.toMutableList().add(Pair(handler.javaClass.name, handler))
    }

    fun handle(job: InboxQueueEntity) {
        val typedObject = jsonConfig.decodeFromString<ApTypedObject>(String(job.content.bytes))

        if (Configuration.debug) logger.debug(
            "Consuming object of type {} from {} on attempt {}",
            typedObject.type,
            transaction { job.sender?.apId ?: "unknown" },
            job.retries + 1
        )

        runBlocking {
            try {
                for (handler in inboxHandlers.filter { it.first == typedObject.type }) {
                    handler.second.handle(job)
                }
                QueueService.Companion.completeInboxJob(job)
            } catch (e: Exception) {
                QueueService.Companion.errorInboxJob(job)
            }
        }
    }

    @ApiStatus.Internal
    fun registerDefault() {
        register("Bite", ApBiteHandler())
    }
}