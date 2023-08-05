import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    id("org.jlleitschuh.gradle.ktlint") version "11.5.0"
    id("org.jlleitschuh.gradle.ktlint-idea") version "11.5.0"
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

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.8")
    implementation("io.github.oshai:kotlin-logging-jvm:5.0.2")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")

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

configurations {
    ktlint
}

ktlint {
    version = "0.50.0"
}
