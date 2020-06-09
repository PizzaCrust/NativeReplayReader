plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.7.0.202003110725-r")
    implementation("commons-io:commons-io:2.6")
    implementation("org.apache.commons:commons-exec:1.3")
}

