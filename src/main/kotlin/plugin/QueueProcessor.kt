package site.remlit.blueb.plugin

import org.pf4j.ExtensionPoint

interface QueueProcessor : ExtensionPoint {
	val activityType: String
	fun process(body: ByteArray)
}
