package site.remlit.aster.event.note

import site.remlit.aster.common.model.Note

/**
 * Event for when a note is created
 *
 * @since 2025.9.1.0-SNAPSHOT
 * */
class NoteCreateEvent(note: Note) : NoteEvent(note)
