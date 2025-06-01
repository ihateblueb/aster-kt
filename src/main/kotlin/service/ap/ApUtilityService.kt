package site.remlit.blueb.service.ap

import com.fasterxml.jackson.core.JsonParseException
import kotlinx.serialization.json.*

class ApUtilityService {
	fun extractString(item: JsonElement?): String? = when (item) {
		is JsonArray -> item.first().jsonPrimitive.toString()
		is JsonPrimitive -> item.jsonPrimitive.contentOrNull
		else -> null
	}

	fun extractBoolean(item: JsonElement?): Boolean? = when (item) {
		is JsonArray -> item.first().jsonPrimitive.booleanOrNull
		is JsonPrimitive -> item.jsonPrimitive.booleanOrNull
		else -> null
	}

	fun extractObject(item: JsonElement?): JsonObject? = item?.jsonObject

	fun byteArrayToObject(byteArray: ByteArray): JsonObject? = try {
		Json.parseToJsonElement(String(byteArray)).jsonObject
	} catch (e: JsonParseException) {
		null
	}
}
