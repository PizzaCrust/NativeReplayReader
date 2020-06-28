plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
    maven("https://jitpack.io/")
}

dependencies {
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.7.0.202003110725-r")
    implementation("commons-io:commons-io:2.6")
    implementation("org.apache.commons:commons-exec:1.3")
    implementation("com.github.PizzaCrust:CSharpKtModelTranspiler:2476215cb8")
    implementation(files("csharp-1.0-SNAPSHOT.jar"))
}

