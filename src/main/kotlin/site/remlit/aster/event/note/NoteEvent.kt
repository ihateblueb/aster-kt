package site.remlit.aster.event.note

import org.jetbrains.annotations.ApiStatus
import site.remlit.aster.common.model.Note
import site.remlit.aster.event.Event

/**
 * Event related to a Note
 *
 * @since 2025.9.1.0-SNAPSHOT
 * */
@ApiStatus.OverrideOnly
abstract class NoteEvent(val note: Note) : Event