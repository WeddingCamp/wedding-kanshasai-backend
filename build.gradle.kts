import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar
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

    // Database
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.2")
    implementation("org.mybatis.dynamic-sql:mybatis-dynamic-sql:1.5.0")
    implementation("com.mysql:mysql-connector-j:8.1.0")
    implementation("de.huxhorn.sulky:de.huxhorn.sulky.ulid:8.3.0")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // gRPC
    implementation("net.devh:grpc-spring-boot-starter:2.14.0.RELEASE")
    implementation("wedding.kanshasai:protobuf:0.0.4")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.8")
    implementation("io.github.oshai:kotlin-logging-jvm:5.0.2")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2:2.2.220")
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

tasks.getByName<BootJar>("bootJar") {
    archiveFileName = "backend.jar"
}

configurations {
    ktlint
}

ktlint {
    version = "0.50.0"
}
