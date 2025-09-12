package site.remlit.blueb.aster.service.ap

import site.remlit.blueb.aster.model.Service
import site.remlit.blueb.aster.model.Visibility

class ApVisibilityService : Service() {
	companion object {
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
