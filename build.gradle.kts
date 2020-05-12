plugins {
    kotlin("jvm") version "1.3.72"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.apache.commons:commons-exec:1.3")
    implementation("org.apache.commons:commons-lang3:3.9")
    implementation("commons-io:commons-io:2.6")
    implementation("com.google.code.gson:gson:2.8.6")
}