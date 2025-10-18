package site.remlit.blueb.aster.event

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.annotations.ApiStatus
import org.slf4j.LoggerFactory
import site.remlit.blueb.aster.model.Configuration
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

object EventRegistry {
	private val logger = LoggerFactory.getLogger(EventRegistry::class.java)

	/**
	 * Mutable list of event classes and functions to run when they're called
	 *
	 * @since 2025.9.1.0-SNAPSHOT
	 * */
	val listeners: MutableList<Pair<KClass<*>, (Event) -> Unit>> =
		emptyList<Pair<KClass<*>, (Event) -> Unit>>().toMutableList()

	/**
	 * Adds a function to be called when an event fires
	 *
	 * @param event Class of the event to listen to (e.g. `NoteCreateEvent::class`)
	 * @param listener Lambda to run when the event happens
	 *
	 * @since 2025.9.1.0-SNAPSHOT
	 * */
	fun addListener(event: KClass<*>, listener: (Event) -> Unit) {
		if (!event.isSubclassOf(Event::class)) throw IllegalArgumentException("Event $event is not a derivative of the Event interface")
		if (Configuration.debug) logger.debug("Added ${event.simpleName} listener ${listener::class.simpleName}")
		listeners.add(Pair(event, listener))
	}

	private val eventScope = CoroutineScope(Dispatchers.Default + CoroutineName("EventCoroutine"))

	/**
	 * Function ran from an Event when it's called through the call() method.
	 *
	 * This function will launch a coroutine that will run listeners for the specified event in order they were registered.
	 *
	 * @param event Instance of an event to be executed
	 *
	 * @since 2025.9.1.0-SNAPSHOT
	 * */
	@ApiStatus.Internal
	fun executeEvent(event: Event) =
		runBlocking {
			eventScope.launch {
				for (listener in listeners) {
					if (!listener.first.isInstance(event)) continue
					listener.second.invoke(event)
				}
			}
		}

	/**
	 * Clears the registered listeners to prevent any events blocking shutdown.
	 * */
	@ApiStatus.Internal
	fun clearListeners() = listeners.clear()
}