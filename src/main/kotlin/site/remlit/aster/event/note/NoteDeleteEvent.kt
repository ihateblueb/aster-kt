package site.remlit.aster.event.note

import site.remlit.aster.common.model.Note

/**
 * Event for when a note is deleted
 *
 * @since 2025.9.1.0-SNAPSHOT
 * */
class NoteDeleteEvent(note: Note) : NoteEvent(note)
