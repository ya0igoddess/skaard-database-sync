val koTySaVersion: String by properties

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":database-sync"))
    implementation("org.ufoss.kotysa:kotysa-r2dbc:$koTySaVersion")
}