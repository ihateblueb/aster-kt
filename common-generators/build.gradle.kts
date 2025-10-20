plugins {
	application
	kotlin("jvm")
	id("com.gradleup.shadow")
}

group = "site.remlit.blueb.common"
version = gradle.extra.get("rootVersion") as String

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("reflect"))
	implementation("com.squareup:kotlinpoet:2.2.0")
	implementation(project(":common"))
}

application {
	mainClass = "site.remlit.blueb.aster.common.generator.GeneratorKt"
}

kotlin {
	jvmToolchain(21)
}

tasks.build {
	dependsOn("shadowJar")
}