package site.remlit.aster.common.model

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class MetaStatCount(
	val local: Long,
	val remote: Long,
	val total: Long = local + remote
)
