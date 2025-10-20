package site.remlit.blueb.aster.model.ap.activity

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import site.remlit.blueb.aster.model.ap.ApObjectWithContext
import site.remlit.blueb.aster.model.ap.ApType

@Serializable
data class ApUpdateActivity(
	val id: String,
	val type: ApType.Activity = ApType.Activity.Update,

	val actor: String? = null,
	val `object`: JsonPrimitive,

	val to: List<String>,
	val cc: List<String>
) : ApObjectWithContext()
