package site.remlit.aster.event

import org.jetbrains.annotations.ApiStatus

/**
 * Interface for listenable events
 * */
@ApiStatus.OverrideOnly
interface Event {
	fun call() = EventRegistry.executeEvent(this)
}