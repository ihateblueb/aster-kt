package site.remlit.blueb.aster.model

import kotlinx.serialization.Serializable

@Serializable
data class WellKnown(
	val subject: String? = null,
	val aliases: List<String>? = null,
	val links: List<WellKnownLink>? = null,
)
