package me.blueb.plugin

import me.blueb.model.ap.ApType
import org.pf4j.ExtensionPoint

interface Processor : ExtensionPoint {
	val activityType: ApType.Activity
	fun process(body: Any)
}
