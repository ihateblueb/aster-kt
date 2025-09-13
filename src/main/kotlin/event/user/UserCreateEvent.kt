package site.remlit.blueb.aster.event.user

import site.remlit.blueb.aster.model.User

/**
 * Event for when a user is created
 *
 * @since 2025.9.1.0-SNAPSHOT
 * */
class UserCreateEvent(user: User) : UserEvent(user)