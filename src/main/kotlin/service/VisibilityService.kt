package site.remlit.blueb.aster.service

import site.remlit.blueb.aster.model.Service
import site.remlit.blueb.aster.model.User
import site.remlit.blueb.aster.model.Visibility

/**
 * Service for handling visibility in non-ActivityPub contexts.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
class VisibilityService : Service() {
	companion object {
		/**
		 * Determines if a user can see an entity
		 *
		 * @param visibility Visibility of the entity
		 * @param author Author of the entity
		 * @param to List of user ids, other users who can see this entity (for [site.remlit.blueb.aster.model.Visibility.Direct]). Not applicable to all entities.
		 * @param user User who is trying to view the entity
		 * @param ignoreBlock Whether to take block relationships into account
		 *
		 * @return If the user can see the entity
		 * */
		fun canISee(
			visibility: Visibility,
			author: String,
			to: List<String>? = null,
			user: String,
			ignoreBlock: Boolean? = false
		): Boolean {
			val author = User.fromEntity(UserService.getById(author) ?: throw Exception("Author not found"))
			val user = User.fromEntity(UserService.getById(user) ?: throw Exception("User not found"))

			if (RelationshipService.eitherBlocking(user.id, author.id))
				return false

			when (visibility) {
				Visibility.Public -> {
					return true
				}

				Visibility.Unlisted -> {
					return true
				}

				Visibility.Followers -> {
					return false
				}

				Visibility.Direct -> {
					return false
				}
			}
		}
	}
}
