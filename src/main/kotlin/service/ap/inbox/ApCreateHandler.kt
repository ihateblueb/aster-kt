package site.remlit.blueb.aster.service.ap.inbox

import org.slf4j.LoggerFactory
import site.remlit.blueb.aster.db.entity.InboxQueueEntity
import site.remlit.blueb.aster.model.ap.ApInboxHandler
import site.remlit.blueb.aster.model.ap.ApNote
import site.remlit.blueb.aster.model.ap.ApTypedObject
import site.remlit.blueb.aster.model.ap.activity.ApCreateActivity
import site.remlit.blueb.aster.util.jsonConfig

class ApCreateHandler : ApInboxHandler() {
	private val logger = LoggerFactory.getLogger(ApCreateHandler::class.java)

	override suspend fun handle(job: InboxQueueEntity) {
		val create = jsonConfig.decodeFromString<ApCreateActivity>(String(job.content.bytes))
		val createCopy = create.copy()

		logger.debug("{}", createCopy.`object`)
		val obj = if (createCopy.`object` is String) return else createCopy.`object` as ApTypedObject

		when (obj.type) {
			"Note" -> handleNote(job, create.`object` as ApNote)
			else -> throw NotImplementedError("No Create handler for ${obj.type}")
		}
	}

	fun handleNote(job: InboxQueueEntity, note: ApNote) {
		println(note)
	}
}