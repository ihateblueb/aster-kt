package site.remlit.aster.model.ap.activity

import kotlinx.serialization.Serializable
import site.remlit.aster.model.ap.ApObjectWithContext
import site.remlit.aster.model.ap.ApType

@Serializable
data class ApBiteActivity(
	val id: String,
	val type: ApType.Activity = ApType.Activity.Bite,

	val actor: String? = null,

	/**
	 * APID for either an actor or a note.
	 * */
	val target: String,
) : ApObjectWithContext()
