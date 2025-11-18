package site.remlit.aster.event.user

import site.remlit.aster.common.model.Relationship
import site.remlit.aster.common.model.User

open class UserRelationshipEvent(val relationship: Relationship, user: User) : UserEvent(user)
