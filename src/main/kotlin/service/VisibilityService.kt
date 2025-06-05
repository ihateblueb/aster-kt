package site.remlit.blueb.aster.service

import site.remlit.blueb.aster.model.Visibility

class VisibilityService {
	private val userService = UserService()
	private val relationshipService = RelationshipService()

	/**
	 * Determines if a user can see an entity
	 *
	 * @param visibility Visibility of the entity
	 * @param author Author ([site.remlit.blueb.model.User]) of the entity
	 * @param to List of [site.remlit.blueb.model.User.id], other users who can see this entity (for [Visibility.Direct]). Not applicable to all entities.
	 * @param user User who is trying to view the entity
	 * @param ignoreBlock Whether to take block relationships into account
	 * */
	suspend fun canISee(
		visibility: Visibility,
		author: String,
		to: List<String>? = null,
		user: String,
		ignoreBlock: Boolean? = false
	): Boolean {
		val author = userService.getById(author)
		val user = userService.getById(user)

		require(author != null, { "Author not found, author must be an existing user's id" })
		require(user != null, { "User not found, user must be an existing user's id" })

		if (relationshipService.eitherBlocking(user.id.toString(), author.id.toString()))
			return false

		when (visibility) {
			Visibility.Public -> {
			}

			Visibility.Unlisted -> {
			}

			Visibility.Followers -> {
			}

			Visibility.Direct -> {
			}
		}

		return true
	}
}
