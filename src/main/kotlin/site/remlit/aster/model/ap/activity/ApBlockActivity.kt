package site.remlit.aster.model.ap.activity

import kotlinx.serialization.Serializable
import site.remlit.aster.model.ap.ApIdOrObject
import site.remlit.aster.model.ap.ApObjectWithContext
import site.remlit.aster.model.ap.ApType

@Serializable
data class ApBlockActivity(
	val id: String,
	val type: ApType.Activity = ApType.Activity.Block,

	val actor: String? = null,
	val `object`: ApIdOrObject,
) : ApObjectWithContext()