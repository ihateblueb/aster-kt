package site.remlit.blueb.aster.common.model

import kotlinx.serialization.Serializable

@Serializable
data class MetaStatCount(
	val local: Long,
	val remote: Long,
	val total: Long = local + remote
)
