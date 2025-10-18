package site.remlit.blueb.aster.event.user

import org.jetbrains.annotations.ApiStatus
import site.remlit.blueb.aster.common.model.User
import site.remlit.blueb.aster.event.Event

/**
 * Event related to a User
 *
 * @since 2025.9.1.0-SNAPSHOT
 * */
@ApiStatus.OverrideOnly
abstract class UserEvent(val user: User) : Event