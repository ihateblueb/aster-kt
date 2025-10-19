rootProject.name = "aster"
gradle.extra.set("rootVersion", "2025.10.4.2-SNAPSHOT")

pluginManagement {
	plugins {
		application
		`maven-publish`

		kotlin("jvm") version "2.2.20"
		kotlin("multiplatform") version "2.2.20"
		kotlin("plugin.serialization") version "2.2.20"
		kotlin("plugin.jsPlainObjects") version "2.2.20"

		id("io.ktor.plugin") version "3.3.1"
		id("com.gradleup.shadow") version "8.3.0"
		id("org.jetbrains.dokka") version "2.0.0"
	}
}

include("common")