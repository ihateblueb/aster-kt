package me.blueb.util

import kotlinx.serialization.json.Json

val jsonConfig = Json {
	encodeDefaults = true
	prettyPrint = true
	isLenient = true
}
