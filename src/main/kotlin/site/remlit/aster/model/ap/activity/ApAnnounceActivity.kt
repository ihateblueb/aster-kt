package site.remlit.aster.model.ap.activity

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import site.remlit.aster.model.ap.ApIdOrObject
import site.remlit.aster.model.ap.ApObjectWithContext
import site.remlit.aster.model.ap.ApType

@Serializable
data class ApAnnounceActivity(
	val id: String,
	val type: ApType.Activity = ApType.Activity.Announce,

	val actor: String? = null,
	val published: LocalDateTime,
	val `object`: ApIdOrObject,

	val to: List<String>,
	val cc: List<String>
) : ApObjectWithContext()
