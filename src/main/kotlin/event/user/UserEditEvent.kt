package site.remlit.blueb.aster.event.user

import site.remlit.blueb.aster.common.model.User

/**
 * Event for when a user is edited
 *
 * @since 2025.9.1.0-SNAPSHOT
 * */
class UserEditEvent(user: User) : UserEvent(user)