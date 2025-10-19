package site.remlit.blueb.aster.common.model

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class MetaStats(
	val users: MetaStatCount,
	val notes: MetaStatCount,
)
