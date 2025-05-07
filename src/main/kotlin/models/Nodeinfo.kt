package me.blueb.models

import kotlinx.serialization.Serializable

@Serializable
data class Nodeinfo(
    val version: String,
    val software: NodeinfoSoftare,
    val protocols: List<String> = listOf("activitypub"),
    val openRegistrations: Boolean,
    val usage: NodeinfoUsage,
)

@Serializable
data class NodeinfoSoftare(
    val name: String,
    val version: String
)

@Serializable
data class NodeinfoUsage(
    val users: NodeinfoUsageUsers,
    val localPosts: Int
)

@Serializable
data class NodeinfoUsageUsers(
    val total: Int,
)