package site.remlit.blueb.aster.model.ap.activity

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import site.remlit.blueb.aster.model.ap.ApObjectWithContext
import site.remlit.blueb.aster.model.ap.ApType

@Serializable
data class ApBiteActivity(
	val id: String,
	val type: ApType.Activity = ApType.Activity.Bite,

	val actor: String? = null,
	val published: LocalDateTime,

	/**
	 * APID for either an actor or a note.
	 * */
	val target: String,
) : ApObjectWithContext()
