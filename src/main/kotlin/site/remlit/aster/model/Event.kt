package site.remlit.aster.model

import org.jetbrains.annotations.ApiStatus
import site.remlit.aster.registry.EventRegistry

/**
 * Interface for listenable events
 * */
@ApiStatus.OverrideOnly
interface Event {
	fun call() = EventRegistry.executeEvent(this)
}