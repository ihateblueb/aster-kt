package site.remlit.blueb.aster.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class RelationshipType {
	@SerialName("block")
	Block,

	@SerialName("mute")
	Mute,

	@SerialName("follow")
	Follow
}
