package site.remlit.blueb.aster.common.model

import kotlinx.serialization.Serializable

@Serializable
data class MetaStats(
	val users: MetaStatCount,
	val notes: MetaStatCount,
)
