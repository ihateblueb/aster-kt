package site.remlit.blueb.plugin

import org.pf4j.ExtensionPoint

interface Processor : ExtensionPoint {
	val activityType: String
	fun process(body: Any)
}
