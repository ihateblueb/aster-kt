package site.remlit.blueb.model

import kotlinx.serialization.Serializable

@Serializable
data class MetaStats(
	val users: MetaStatCount,
	val notes: MetaStatCount,
)
