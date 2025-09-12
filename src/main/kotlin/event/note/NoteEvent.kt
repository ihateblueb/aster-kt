package site.remlit.blueb.aster.event.note

import org.jetbrains.annotations.ApiStatus
import site.remlit.blueb.aster.event.Event
import site.remlit.blueb.aster.model.Note

/**
 * Event related to a Note
 *
 * @since 2025.9.1.0-SNAPSHOT
 * */
@ApiStatus.OverrideOnly
abstract class NoteEvent(val note: Note) : Event