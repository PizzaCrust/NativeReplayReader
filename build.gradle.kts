plugins {
    kotlin("multiplatform") version "1.3.72"
}

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    js()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("org.apache.commons:commons-exec:1.3")
                implementation("org.apache.commons:commons-lang3:3.9")
                implementation("commons-io:commons-io:2.6")
                implementation("com.google.code.gson:gson:2.8.6")
                compileOnly("org.eclipse.jgit:org.eclipse.jgit:5.7.0.202003110725-r")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }
    }
}