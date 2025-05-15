package me.blueb.service.ap

import me.blueb.model.Visibility

class ApVisibilityService {
	fun visibilityToCc(visibility: Visibility, followersUrl: String?, to: List<String>?): Map<String, List<String>> {
		return when(visibility) {
			Visibility.Public -> mapOf(
				Pair(
					"to",
					listOf("https://www.w3.org/ns/activitystreams#Public")
				),
				Pair(
					"cc",
					listOf()
				),
			)
			Visibility.Unlisted -> mapOf(
				Pair(
					"to",
					if (followersUrl != null) listOf(followersUrl) else listOf()
				),
				Pair(
					"cc",
					listOf("https://www.w3.org/ns/activitystreams#Public")
				),
			)
			Visibility.Followers -> mapOf(
				Pair(
					"to",
					if (followersUrl != null) listOf(followersUrl) else listOf()
				),
				Pair(
					"cc",
					listOf()
				),
			)
			Visibility.Direct -> mapOf(
				Pair(
					"to",
					to ?: listOf()
				),
				Pair(
					"cc",
					listOf()
				),
			)
		}
	}
}
