package site.remlit.blueb.aster.common.model

import kotlinx.serialization.Serializable
import site.remlit.blueb.aster.common.model.type.InstanceRegistrationsType
import kotlin.js.JsExport

@JsExport
@Serializable
data class Meta(
	val name: String,
	val version: MetaVersion,
	val plugins: Map<String, String> = mapOf(),
	val registrations: InstanceRegistrationsType,
	val stats: MetaStats,
	val staff: MetaStaff
)
