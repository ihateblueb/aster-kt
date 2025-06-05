package site.remlit.blueb.aster.model

import kotlinx.serialization.Serializable

@Serializable
data class NodeInfoUsage(
	val users: NodeInfoUsageUsers,
	val localPosts: Int,
)
