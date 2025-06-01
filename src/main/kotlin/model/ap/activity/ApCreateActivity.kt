package site.remlit.blueb.model.activity

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import site.remlit.blueb.model.ap.ApObjectWithContext
import site.remlit.blueb.model.ap.ApType

@Serializable
data class ApCreateActivity(
	val type: ApType.Activity = ApType.Activity.Create,
	val actor: String? = null,

	@Contextual
	val `object`: Any
) : ApObjectWithContext()
