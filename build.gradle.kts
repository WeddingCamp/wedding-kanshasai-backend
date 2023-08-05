import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
	id("org.springframework.boot") version "3.1.2"
	id("io.spring.dependency-management") version "1.1.2"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}

group = "wedding.kanshasai"
version = "0.0.1"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
	maven {
		url = URI("https://nexus.mizucoffee.net/repository/maven-public/")
	}
}

dependencies {
	// Spring
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("net.devh:grpc-spring-boot-starter:2.14.0.RELEASE")

	// Kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// gRPC
	implementation("wedding.kanshasai:protobuf:0.0.1")

	// Test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
