package site.remlit.blueb.aster.event.note

import site.remlit.blueb.aster.common.model.Note
import site.remlit.blueb.aster.common.model.User

/**
 * Event for when a note is liked by a user
 *
 * @param note Note liked
 * @param user User who liked the note
 *
 * @since 2025.9.1.1-SNAPSHOT
 * */
class NoteLikeEvent(note: Note, user: User) : NoteInteractionEvent(note, user)