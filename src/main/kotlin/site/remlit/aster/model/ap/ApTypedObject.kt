package site.remlit.aster.model.ap

import kotlinx.serialization.Serializable

@Serializable
open class ApTypedObject(
	val type: String,
) : ApObjectWithContext(), ApObject