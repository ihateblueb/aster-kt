package site.remlit.aster.event.user

import site.remlit.aster.common.model.User

/**
 * Event for when a user is suspended
 *
 * @since 2025.9.1.0-SNAPSHOT
 * */
class UserSuspendEvent(user: User) : UserEvent(user)
