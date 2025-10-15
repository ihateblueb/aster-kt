plugins {
	application
	`maven-publish`

	kotlin("jvm") version "2.2.20"
	kotlin("plugin.serialization") version "2.2.20"

	id("io.ktor.plugin") version "3.3.1"
	id("com.gradleup.shadow") version "8.3.0"
	id("org.jetbrains.dokka") version "2.0.0"
}

group = "site.remlit.blueb"
version = "2025.10.1.0-SNAPSHOT"

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
	implementation("org.jetbrains.kotlin:kotlin-reflect:2.2.20")

	implementation("ch.qos.logback:logback-classic:1.5.19")
	implementation("org.slf4j:slf4j-api:2.0.17")

	// ktor server
	implementation("io.ktor:ktor-server-core-jvm:3.3.1")
	implementation("io.ktor:ktor-server-netty-jvm:3.3.1")
	implementation("io.ktor:ktor-server-auth:3.3.1")
	implementation("io.ktor:ktor-server-config-yaml-jvm:3.3.1")
	implementation("io.ktor:ktor-server-core:3.3.1")
	implementation("io.ktor:ktor-server-openapi:3.3.1")
	implementation("io.ktor:ktor-server-swagger:3.3.1")
	implementation("io.ktor:ktor-server-csrf:3.3.1")
	implementation("io.ktor:ktor-server-call-logging:3.3.1")
	implementation("io.ktor:ktor-server-request-validation-jvm:3.3.1")
	implementation("io.ktor:ktor-server-request-validation:3.3.1")
	implementation("io.ktor:ktor-server-call-id-jvm:3.3.1")
	implementation("io.ktor:ktor-server-call-id:3.3.1")
	implementation("io.ktor:ktor-server-cors-jvm:3.3.1")
	implementation("io.ktor:ktor-server-call-logging-jvm:3.3.1")
	implementation("io.ktor:ktor-server-default-headers:3.3.1")
	implementation("io.ktor:ktor-server-default-headers-jvm:3.3.1")
	implementation("io.ktor:ktor-server-forwarded-header:3.3.1")
	implementation("io.ktor:ktor-server-forwarded-header-jvm:3.3.1")

	// templating
	implementation("io.ktor:ktor-server-html-builder:3.3.1")
	implementation("org.jetbrains.kotlinx:kotlinx-html:0.12.0")

	// serialization
	implementation("io.ktor:ktor-server-content-negotiation-jvm:3.3.1")
	implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:3.3.1")
	implementation("tools.jackson.core:jackson-core:3.0.0")
	implementation("tools.jackson.core:jackson-databind:3.0.0")

	// ktor client
	implementation("io.ktor:ktor-client-content-negotiation-jvm:3.3.1")
	implementation("io.ktor:ktor-client-core-jvm:3.3.1")
	implementation("io.ktor:ktor-client-cio-jvm:3.3.1")
	implementation("io.ktor:ktor-server-status-pages-jvm:3.3.1")
	implementation("io.ktor:ktor-server-auto-head-response-jvm:3.3.1")

	// database
	implementation("com.zaxxer:HikariCP:7.0.2")
	implementation("org.postgresql:postgresql:42.7.8")
	implementation("org.jetbrains.exposed:exposed-core:1.0.0-rc-1")
	implementation("org.jetbrains.exposed:exposed-dao:1.0.0-rc-1")
	implementation("org.jetbrains.exposed:exposed-jdbc:1.0.0-rc-1")
	implementation("org.jetbrains.exposed:exposed-json:1.0.0-rc-1")
	implementation("org.jetbrains.exposed:exposed-kotlin-datetime:1.0.0-rc-1")
	implementation("org.jetbrains.exposed:exposed-migration-core:1.0.0-rc-1")
	implementation("org.jetbrains.exposed:exposed-migration-jdbc:1.0.0-rc-1")

	// misc
	implementation("at.favre.lib:bcrypt:0.10.2")
	implementation("com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer:20240325.1")
	implementation("org.apache.commons:commons-text:1.14.0")

	implementation("site.remlit.blueb:http-signature-utility:2025.10.4-SNAPSHOT")

	compileOnly("org.jetbrains:annotations:26.0.2-1")
}

kotlin {
	jvmToolchain(21)
}

application {
	mainClass = "site.remlit.blueb.aster.ApplicationKt"
}

ktor {}

// docs

val sourcesJar by tasks.registering(Jar::class) {
	archiveBaseName = "aster"
	archiveClassifier = "sources"
	from(sourceSets.main.get().allSource)
}

val dokkaJavadocZip by tasks.registering(Zip::class) {
	archiveBaseName = "aster"
	archiveClassifier = "javadoc"
	dependsOn(tasks.dokkaJavadoc)
	from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
}

val dokkaHtmlZip by tasks.registering(Zip::class) {
	archiveBaseName = "aster"
	archiveClassifier = "dokka"
	dependsOn(tasks.dokkaHtml)
	from(tasks.dokkaHtml.map { it.outputDirectory })
}

artifacts {
	add("archives", sourcesJar)
	add("archives", dokkaJavadocZip)
	add("archives", dokkaHtmlZip)
}

// building

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

// publishing

tasks.publish {
	dependsOn("sourcesJar")
	dependsOn("dokkaHtml")
	dependsOn("javadoc")
}

publishing {
	repositories {
		maven {
			name = "remlitSiteMain"
			url = if (version.toString()
					.contains("SNAPSHOT")
			) uri("https://repo.remlit.site/snapshots") else uri("https://repo.remlit.site/releases")

			credentials {
				username = System.getenv("REPO_ACTOR")
				password = System.getenv("REPO_TOKEN")
			}
		}
	}
	publications {
		create<MavenPublication>("maven") {
			groupId = "site.remlit.blueb"
			artifactId = "aster"
			version = project.version.toString()

			from(components["java"])

			artifact(sourcesJar)
			artifact(dokkaJavadocZip)
			artifact(dokkaHtmlZip)

			pom {
				name = "aster"
				url = "https://github.com/ihateblueb/aster-kt"

				licenses {
					license {
						name = "AGPLv3 License"
						url = "https://opensource.org/license/agpl-v3"
					}
				}

				developers {
					developer {
						id = "ihateblueb"
						name = "ihateblueb"
						email = "ihateblueb@proton.me"
					}
				}

				scm {
					connection = "scm:git:git://github.com/ihateblueb/aster-kt.git"
					developerConnection = "scm:git:ssh://github.com/ihateblueb/aster-kt.git"
					url = "https://github.com/ihateblueb/aster-kt"
				}
			}
		}
	}
}