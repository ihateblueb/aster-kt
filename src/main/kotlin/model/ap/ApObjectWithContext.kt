package site.remlit.blueb.aster.model.ap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import org.jetbrains.annotations.ApiStatus

@Serializable
@ApiStatus.OverrideOnly
abstract class ApObjectWithContext(
	@SerialName("@context")
	val context: JsonArray = ApContext
) : ApObject
