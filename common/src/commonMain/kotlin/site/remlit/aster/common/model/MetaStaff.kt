package site.remlit.aster.common.model

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class MetaStaff(
	val admin: List<site.remlit.aster.common.model.SmallUser?> = listOf(),
	val mod: List<site.remlit.aster.common.model.SmallUser?> = listOf()
)
