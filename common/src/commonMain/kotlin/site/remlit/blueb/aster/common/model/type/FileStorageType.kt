package site.remlit.blueb.aster.common.model.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
enum class FileStorageType {
	@SerialName("local")
	Local
}