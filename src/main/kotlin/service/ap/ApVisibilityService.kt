package site.remlit.blueb.aster.service.ap

import site.remlit.blueb.aster.model.Service
import site.remlit.blueb.aster.model.Visibility

/**
 * Service for understanding visibility in ActivityPub terms.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
class ApVisibilityService : Service() {
	companion object {

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
					"to" to listOf("https://www.w3.org/ns/activitystreams#Public"),
					"cc" to emptyList()
				)

				Visibility.Unlisted -> mapOf(
					"to" to if (followersUrl != null) listOf(followersUrl) else listOf(),
					"cc" to listOf("https://www.w3.org/ns/activitystreams#Public")
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
	}
}
