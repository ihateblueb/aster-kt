package site.remlit.blueb.aster.model.ap.activity

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import site.remlit.blueb.aster.model.ap.ApObjectWithContext
import site.remlit.blueb.aster.model.ap.ApType

@Serializable
data class ApDeleteActivity(
	val type: ApType.Activity = ApType.Activity.Delete,
	val actor: String? = null,
	val `object`: JsonObject
) : ApObjectWithContext()
