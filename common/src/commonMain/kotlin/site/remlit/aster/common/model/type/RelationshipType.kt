package site.remlit.aster.common.model.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
enum class RelationshipType {
	@SerialName("block")
	Block,

	@SerialName("mute")
	Mute,

	@SerialName("follow")
	Follow
}
