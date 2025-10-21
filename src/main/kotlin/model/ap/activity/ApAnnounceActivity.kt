package site.remlit.blueb.aster.model.ap.activity

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import site.remlit.blueb.aster.model.ap.ApObjectWithContext
import site.remlit.blueb.aster.model.ap.ApType

@Serializable
data class ApAnnounceActivity(
	val id: String,
	val type: ApType.Activity = ApType.Activity.Announce,

	val actor: String? = null,
	val published: LocalDateTime,
	@Contextual val `object`: Any,

	val to: List<String>,
	val cc: List<String>
) : ApObjectWithContext()
