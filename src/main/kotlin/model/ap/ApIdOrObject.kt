package site.remlit.blueb.aster.model.ap

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import site.remlit.blueb.aster.util.jsonConfig
import site.remlit.blueb.aster.util.serialization.ApIdOrObjectSerializer

@Serializable(with = ApIdOrObjectSerializer::class)
sealed class ApIdOrObject {
	@Serializable
	data class Id(val value: String) : ApIdOrObject()

	@Serializable
	data class Object(
		@Polymorphic val value: JsonObject,
	) : ApIdOrObject()

	companion object {
		fun createObject(json: JsonObject) = Object(json)

		inline fun <reified T> createObject(obj: () -> T) = createObject(
			jsonConfig.encodeToJsonElement<T>(obj()) as JsonObject
		)
	}
}