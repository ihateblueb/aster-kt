package site.remlit.blueb.aster.model.ap.activity

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import site.remlit.blueb.aster.model.ap.ApObjectWithContext
import site.remlit.blueb.aster.model.ap.ApType

@Serializable
data class ApBlockActivity(
	val id: String,
	val type: ApType.Activity = ApType.Activity.Block,

	val actor: String? = null,
	@Contextual val `object`: Any,
) : ApObjectWithContext()