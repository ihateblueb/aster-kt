package site.remlit.blueb.model

import kotlinx.serialization.Serializable

@Serializable
data class NodeInfoUsage(
	val users: NodeInfoUsageUsers,
	val localPosts: Int,
)
