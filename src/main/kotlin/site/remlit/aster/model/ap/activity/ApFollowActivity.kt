package site.remlit.aster.model.ap.activity

import kotlinx.serialization.Serializable
import site.remlit.aster.model.ap.ApIdOrObject
import site.remlit.aster.model.ap.ApObjectWithContext
import site.remlit.aster.model.ap.ApType

@Serializable
data class ApFollowActivity(
	val id: String,
	val type: ApType.Activity = ApType.Activity.Follow,

	val actor: String,
	val `object`: ApIdOrObject,
) : ApObjectWithContext()
