package site.remlit.blueb.aster.common.model

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class MetaStaff(
	val admin: List<SmallUser?> = listOf(),
	val mod: List<SmallUser?> = listOf()
)
