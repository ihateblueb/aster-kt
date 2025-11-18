package site.remlit.aster.model.ap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import org.jetbrains.annotations.ApiStatus

/**
 * Automatically appends context to the object.
 * Needed for activities and entities.
 * */
@Serializable
@ApiStatus.OverrideOnly
open class ApObjectWithContext(
	@SerialName("@context")
	val context: JsonArray = ApContext
) : ApObject
