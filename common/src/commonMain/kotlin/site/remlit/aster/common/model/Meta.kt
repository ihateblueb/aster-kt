package site.remlit.aster.common.model

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class Meta(
	val name: String,
	val version: MetaVersion,
	val plugins: Map<String, String> = mapOf(),
	val registrations: site.remlit.aster.common.model.type.InstanceRegistrationsType,
	val stats: MetaStats,
	val staff: MetaStaff
)
