package site.remlit.blueb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Visibility {
	/**
	 * Visible to anyone.
	 * */
	@SerialName("public")
	Public,

	/**
	 * Visible to anyone, but not shown on "local" or "public" timeline.
	 * */
	@SerialName("unlisted")
	Unlisted,

	/**
	 * Visible only to followers.
	 * */
	@SerialName("followers")
	Followers,

	/**
	 * Visible only to people mentioned (in the `to` array).
	 * */
	@SerialName("direct")
	Direct;

	companion object {
		fun fromString(value: String): Visibility {
			return when (value) {
				"public" -> Public
				"unlisted" -> Unlisted
				"followers" -> Followers
				"direct" -> Direct
				else -> Direct
			}
		}
	}
}
