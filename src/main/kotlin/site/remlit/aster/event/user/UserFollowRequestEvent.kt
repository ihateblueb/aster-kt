package site.remlit.aster.event.user

import site.remlit.aster.common.model.Relationship
import site.remlit.aster.common.model.User

class UserFollowRequestEvent(relationship: Relationship, user: User) : UserRelationshipEvent(relationship, user)