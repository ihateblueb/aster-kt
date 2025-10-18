plugins {
	kotlin("jvm") version "2.2.20"
	kotlin("plugin.serialization") version "2.2.20"
}

group = "site.remlit.blueb.aster"
version = gradle.extra.get("rootVersion") as String

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
	compileOnly("org.jetbrains:annotations:26.0.2-1")
}

kotlin {
	jvmToolchain(21)
}