plugins {
	application
	kotlin("jvm") version "2.2.0"
	kotlin("plugin.serialization") version "2.2.0"
	id("com.gradleup.shadow") version "8.3.0"
}

repositories {
	mavenCentral()
	maven {
		url = uri("https://repo.remlit.site/releases")
	}
	maven {
		url = uri("https://repo.remlit.site/snapshots")
	}
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect:2.2.0")

	implementation("ch.qos.logback:logback-classic:1.5.18")
	implementation("org.slf4j:slf4j-api:2.0.17")

	// ktor server
	implementation("io.ktor:ktor-server-core-jvm:3.2.2")
	implementation("io.ktor:ktor-server-netty-jvm:3.2.2")
	implementation("io.ktor:ktor-server-config-yaml-jvm:3.2.2")
	implementation("io.ktor:ktor-server-core:3.2.2")
	implementation("io.ktor:ktor-server-openapi:3.2.2")
	implementation("io.ktor:ktor-server-csrf:3.2.2")
	implementation("io.ktor:ktor-server-call-logging:3.2.2")
	implementation("io.ktor:ktor-server-request-validation-jvm:3.2.2")
	implementation("io.ktor:ktor-server-swagger-jvm:3.2.2")
	implementation("io.ktor:ktor-server-request-validation:3.2.2")
	implementation("io.ktor:ktor-server-call-id-jvm:3.2.2")
	implementation("io.ktor:ktor-server-call-id:3.2.2")
	implementation("io.ktor:ktor-server-auth:3.2.2")
	implementation("io.ktor:ktor-server-auth-jvm:3.2.2")
	implementation("io.ktor:ktor-server-cors-jvm:3.2.2")
	implementation("io.ktor:ktor-server-call-logging-jvm:3.2.2")
	implementation("io.ktor:ktor-server-default-headers:3.2.2")
	implementation("io.ktor:ktor-server-default-headers-jvm:3.2.2")
	implementation("io.ktor:ktor-server-forwarded-header:3.2.2")
	implementation("io.ktor:ktor-server-forwarded-header-jvm:3.2.2")

	// serialization
	implementation("io.ktor:ktor-server-content-negotiation-jvm:3.2.2")
	implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:3.2.2")
	implementation("com.fasterxml.jackson.core:jackson-core:2.19.2")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.19.2")

	// ktor client
	implementation("io.ktor:ktor-client-content-negotiation-jvm:3.2.2")
	implementation("io.ktor:ktor-client-core-jvm:3.2.2")
	implementation("io.ktor:ktor-client-cio-jvm:3.2.2")
	implementation("io.ktor:ktor-server-status-pages-jvm:3.2.2")
	implementation("io.ktor:ktor-server-auto-head-response-jvm:3.2.2")

	// database
	implementation("com.zaxxer:HikariCP:6.3.0")
	implementation("org.postgresql:postgresql:42.7.7")
	implementation("io.github.vgv:kolbasa:0.120.0")
	implementation("org.jetbrains.exposed:exposed-core:0.61.0")
	implementation("org.jetbrains.exposed:exposed-dao:0.61.0")
	implementation("org.jetbrains.exposed:exposed-jdbc:0.61.0")
	implementation("org.jetbrains.exposed:exposed-json:0.61.0")
	implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.61.0")
	implementation("org.jetbrains.exposed:exposed-migration:0.61.0")
	implementation("org.flywaydb:flyway-core:11.10.4")
	implementation("org.flywaydb:flyway-database-postgresql:11.10.4")

	// misc
	implementation("at.favre.lib:bcrypt:0.10.2")
	implementation("com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer:20240325.1")
	implementation("org.apache.commons:commons-text:1.14.0")

	implementation("site.remlit.blueb:http-signature-utility:2025.7.2.9-SNAPSHOT")
}

group = "site.remlit.blueb"
version = "2025.9.1.0-SNAPSHOT"

kotlin {
	jvmToolchain(21)
}

application {
	mainClass = "site.remlit.blueb.aster.ApplicationKt"
}

tasks.register<Exec>("cleanFrontend") {
	executable("./scripts/clean-frontend.sh")
}

tasks.clean {
	dependsOn("cleanFrontend")
}

tasks.register<Exec>("compileFrontend") {
	executable("./scripts/build-frontend.sh")
}

tasks.processResources {
	val name = project.provider { project.name }.get()
	val group = project.provider { project.group.toString() }.get()
	val version = project.provider { project.version.toString() }.get()

	filter { line ->
		line.replace("%artifactId%", name)
			.replace("%version%", version)
			.replace("%groupId%", group)
	}
}

tasks.shadowJar {
	dependsOn("compileFrontend")
	dependsOn("processResources")
}

tasks.build {
	dependsOn("shadowJar")
}
