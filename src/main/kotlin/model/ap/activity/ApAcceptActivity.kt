package site.remlit.blueb.aster.model.ap.activity

import kotlinx.serialization.Serializable
import site.remlit.blueb.aster.model.ap.ApIdOrObject
import site.remlit.blueb.aster.model.ap.ApObjectWithContext
import site.remlit.blueb.aster.model.ap.ApType

@Serializable
data class ApAcceptActivity(
	val id: String,
	val type: ApType.Activity = ApType.Activity.Accept,

	val actor: String? = null,
	val `object`: ApIdOrObject,
) : ApObjectWithContext()
