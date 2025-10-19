package site.remlit.blueb.aster.common.model

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class MetaVersion(
	val aster: String,
	val java: String,
	val kotlin: String,
	val system: String,
)
