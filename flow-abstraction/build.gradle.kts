plugins {
    kotlin("jvm")
//    kotlin("plugin.spring")
//    id("org.springframework.boot")
//    id("io.spring.dependency-management")
}

group = "ru.vood.flow"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {

    // https://mvnrepository.com/artifact/io.arrow-kt/arrow-core
    implementation(platform("io.arrow-kt:arrow-stack:2.1.1"))
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.10.2"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework:spring-web:6.2.5")
    implementation("org.springframework:spring-webflux:6.2.5")
    api("io.arrow-kt:arrow-core-jvm:2.1.1")

    testImplementation(kotlin("test"))
//    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.7-1.4-M3")
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("com.ocadotechnology.gembus:test-arranger:1.6.4")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
