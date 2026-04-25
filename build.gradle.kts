plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("pl.allegro.tech.build.axion-release") version "1.18.16"
}

group = "com.fredericoapolonia"
version = scmVersion.version
description = "coverflex-sure-sync"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-restclient")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("tools.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-restclient-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

fun getDockerPath() = ProcessBuilder("which", "docker")
    .start()
    .inputStream.bufferedReader().readLine() ?: "docker"

fun getDockerImageName() = "fredericoapolonia/coverflex-sure-sync:${version}"

tasks.register<Exec>("buildDockerArm") {
    dependsOn("bootJar")
    commandLine(
        getDockerPath(), "buildx", "build",
        "--platform", "linux/arm64",
        "-t", getDockerImageName(),
        "--load",
        "."
    )
}

tasks.register<Exec>("buildDockerX86") {
    dependsOn("bootJar")
    commandLine(
        getDockerPath(), "buildx", "build",
        "--platform", "linux/amd64",
        "-t", getDockerImageName(),
        "--load",
        "."
    )
}

tasks.register<Exec>("pushDocker") {
    val dockerImageName = getDockerImageName()
    dependsOn("bootJar")
    commandLine(
        getDockerPath(), "buildx", "build",
        "--platform", "linux/amd64,linux/arm64",
        "-t", dockerImageName,
        "-t", "fredericoapolonia/coverflex-sure-sync:latest",
        "--push",
        "."
    )
    doLast {
        println("Pushed $dockerImageName")
    }
}