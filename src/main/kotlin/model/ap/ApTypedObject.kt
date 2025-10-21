package site.remlit.blueb.aster.model.ap

import kotlinx.serialization.Serializable

@Serializable
data class ApTypedObject(
	val type: String,
) : ApObject