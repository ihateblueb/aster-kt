@file:OptIn(ExperimentalDistributionDsl::class)

import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl

plugins {
	kotlin("multiplatform")
	kotlin("plugin.serialization")
}

group = "site.remlit.blueb.aster"
version = gradle.extra.get("rootVersion") as String

repositories {
	mavenCentral()
}

kotlin {
	jvm()

	js {
		nodejs()
		useEsModules()
		generateTypeScriptDefinitions()
		binaries.library()
		outputModuleName.set("aster-common")
	}

	sourceSets {
		commonMain.dependencies {
			implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
			implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
		}
	}

	compilerOptions {
		freeCompilerArgs.add("-Xes-long-as-bigint")
		freeCompilerArgs.add("-opt-in=kotlin.js.ExperimentalJsExport")
	}
}