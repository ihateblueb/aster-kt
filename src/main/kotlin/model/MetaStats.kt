package site.remlit.blueb.aster.model

import kotlinx.serialization.Serializable

@Serializable
data class MetaStats(
	val users: MetaStatCount,
	val notes: MetaStatCount,
)
