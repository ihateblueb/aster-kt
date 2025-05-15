package me.blueb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Visibility {
	@SerialName("public")
	Public,

	@SerialName("unlisted")
	Unlisted,

	@SerialName("followers")
	Followers,

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
