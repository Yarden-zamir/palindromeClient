import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
val ktorVersion = "1.6.8"

plugins {
    kotlin("jvm") version "1.6.10"
    application
}

group = "me.charcoal"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}