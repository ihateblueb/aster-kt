package site.remlit.blueb.aster.util

import kotlinx.serialization.json.Json

/**
 * JSON Serialization configuration used for Ktor server and client
 * */
val jsonConfig = Json {
	encodeDefaults = true
	prettyPrint = true
	isLenient = true
}
