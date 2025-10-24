package site.remlit.aster.common.model

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class MetaStats(
	val users: site.remlit.aster.common.model.MetaStatCount,
	val notes: site.remlit.aster.common.model.MetaStatCount,
)
