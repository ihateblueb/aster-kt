plugins {
	application
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("com.gradleup.shadow")
}

group = "site.remlit.aster.common"
version = gradle.extra.get("rootVersion") as String

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("reflect"))
	implementation("com.squareup:kotlinpoet:2.2.0")
	implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
	implementation(project(":common"))
}

application {
	mainClass = "site.remlit.aster.common.generator.GeneratorKt"
}

kotlin {
	jvmToolchain(21)
}

tasks.build {
	dependsOn("shadowJar")
}