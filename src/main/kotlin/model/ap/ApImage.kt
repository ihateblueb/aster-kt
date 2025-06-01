package site.remlit.blueb.model.ap

import kotlinx.serialization.Serializable

@Serializable
data class ApImage(
	val type: ApType.Object = ApType.Object.Image,
	val src: String,
	val sensitive: Boolean = false,
	val name: String? = null,
	val alt: String? = null,
)
