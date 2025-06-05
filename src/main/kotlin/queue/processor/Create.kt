package site.remlit.blueb.queue.processor

import org.slf4j.LoggerFactory
import site.remlit.blueb.plugin.QueueProcessor
import site.remlit.blueb.service.ap.ApUtilityService

class Create(override val activityType: String = "Create") : QueueProcessor {
	private val logger = LoggerFactory.getLogger(this::class.java)

	private val apUtilityService = ApUtilityService()

	override fun process(body: ByteArray) {
		val bodyObject = apUtilityService.byteArrayToObject(body)

		logger.info("Received Create: ${bodyObject?.values?.joinToString(";")}}")
	}
}
