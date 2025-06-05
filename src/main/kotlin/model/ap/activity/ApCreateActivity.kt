package site.remlit.blueb.aster.model.activity

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import site.remlit.blueb.aster.model.ap.ApObjectWithContext
import site.remlit.blueb.aster.model.ap.ApType

@Serializable
data class ApCreateActivity(
	val type: ApType.Activity = ApType.Activity.Create,
	val actor: String? = null,

	@Contextual
	val `object`: Any
) : ApObjectWithContext()
