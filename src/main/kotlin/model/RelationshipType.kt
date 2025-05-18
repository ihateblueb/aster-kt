package me.blueb.model

import kotlinx.serialization.Serializable

@Serializable
enum class RelationshipType {
	Block, Mute, Follow
}
