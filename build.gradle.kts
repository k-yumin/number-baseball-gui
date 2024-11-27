plugins {
    kotlin("jvm") version "2.0.21"
}

group = "yt.yacht"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.formdev:flatlaf:3.5.2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}