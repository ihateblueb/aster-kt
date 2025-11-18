package site.remlit.aster.event.note

import site.remlit.aster.common.model.Note
import site.remlit.aster.common.model.User

/**
 * Event for when a note is repeated by a user
 *
 * @param repeat Repeat note
 * @param note Note repeated
 * @param user Author of repeat note
 *
 * @since 2025.11.2.0-SNAPSHOT
 * */
class NoteRepeatEvent(val repeat: Note, note: Note, user: User) : NoteInteractionEvent(note, user)
