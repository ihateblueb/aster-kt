package site.remlit.blueb.aster.event.note

import org.jetbrains.annotations.ApiStatus
import site.remlit.blueb.aster.common.model.Note
import site.remlit.blueb.aster.common.model.User

/**
 * Basic form of an interaction with a Note
 *
 * @since 2025.9.1.0-SNAPSHOT
 * */
@ApiStatus.OverrideOnly
abstract class NoteInteractionEvent(note: Note, val user: User) : NoteEvent(note)