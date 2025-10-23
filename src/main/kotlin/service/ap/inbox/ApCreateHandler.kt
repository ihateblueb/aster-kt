package site.remlit.blueb.aster.service.ap.inbox

import kotlinx.serialization.json.decodeFromJsonElement
import org.slf4j.LoggerFactory
import site.remlit.blueb.aster.db.entity.InboxQueueEntity
import site.remlit.blueb.aster.model.ap.ApIdOrObject
import site.remlit.blueb.aster.model.ap.ApInboxHandler
import site.remlit.blueb.aster.model.ap.ApNote
import site.remlit.blueb.aster.model.ap.ApTypedObject
import site.remlit.blueb.aster.model.ap.activity.ApCreateActivity
import site.remlit.blueb.aster.service.ap.ApNoteService
import site.remlit.blueb.aster.util.jsonConfig

class ApCreateHandler : ApInboxHandler() {
	private val logger = LoggerFactory.getLogger(ApCreateHandler::class.java)

	override suspend fun handle(job: InboxQueueEntity) {
		val create = jsonConfig.decodeFromString<ApCreateActivity>(String(job.content.bytes))
		val copy = create.copy()

		val obj = when (copy.`object`) {
			is ApIdOrObject.Id -> TODO()
			is ApIdOrObject.Object -> jsonConfig.decodeFromJsonElement<ApTypedObject>(copy.`object`.value)
		}

		when (obj.type) {
			"Note" -> handleNote(job, jsonConfig.decodeFromJsonElement<ApNote>(copy.`object`.value))
			else -> throw NotImplementedError("No Create handler for ${obj.type}")
		}
	}

	suspend fun handleNote(job: InboxQueueEntity, note: ApNote) {
		ApNoteService.resolve(note.id)
			?: throw Exception("Note ${note.id} not found")
	}
}