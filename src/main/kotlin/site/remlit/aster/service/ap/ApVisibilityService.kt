package site.remlit.aster.service.ap

import site.remlit.aster.common.model.Visibility
import site.remlit.aster.model.Service

/**
 * Service for understanding visibility in ActivityPub terms.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
object ApVisibilityService : Service {
	const val AS_PUBLIC = "https://www.w3.org/ns/activitystreams#Public"

	/**
	 * Convert a visibility to the `to` and `cc` fields of an ActivityPub object.
	 *
	 * @param visibility Visibility
	 * @param followersUrl URL of the actor's followers collection
	 * @param to List of other actors this object is addressed to, using their IDs
	 * */
	fun visibilityToCc(
		visibility: Visibility,
		followersUrl: String?,
		to: List<String>?
	): Map<String, List<String>> {
		return when (visibility) {
			Visibility.Public -> mapOf(
				"to" to listOf(AS_PUBLIC),
				"cc" to emptyList()
			)

			Visibility.Unlisted -> mapOf(
				"to" to if (followersUrl != null) listOf(followersUrl) else listOf(),
				"cc" to listOf(AS_PUBLIC)
			)

			Visibility.Followers -> mapOf(
				"to" to if (followersUrl != null) listOf(followersUrl) else listOf(),
				"cc" to emptyList()
			)

			Visibility.Direct -> mapOf(
				"to" to emptyList(), // todo: to
				"cc" to emptyList()
			)
		}
	}

	fun determineVisibility(
		to: List<String>,
		cc: List<String>,
		followersUrl: String?,
		visibility: String? = null,
	): Visibility {
		when (visibility) {
			"public" -> return Visibility.Public
			"unlisted" -> return Visibility.Unlisted
			"followers" -> return Visibility.Followers
			"direct" -> return Visibility.Direct
		}

		if (to.contains(followersUrl) && !to.contains(AS_PUBLIC))
			return Visibility.Followers

		if (to.contains(followersUrl) && cc.contains(AS_PUBLIC))
			return Visibility.Unlisted

		if (to.contains(AS_PUBLIC))
			return Visibility.Public

		return Visibility.Direct
	}
}
