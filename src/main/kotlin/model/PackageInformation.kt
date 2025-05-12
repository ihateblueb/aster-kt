package me.blueb.model

import io.ktor.server.config.ApplicationConfig

class PackageInformation {
	val name: String
	val version: String
	val groupId: String

	init {
		val appConfig = ApplicationConfig("application.yaml")

		name = appConfig.propertyOrNull("name")?.getString() ?: "aster"
		version = appConfig.propertyOrNull("version")?.getString() ?: "0.0.0"
		groupId = appConfig.propertyOrNull("groupId")?.getString() ?: "me.blueb"
	}
}
