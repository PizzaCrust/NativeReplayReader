task("buildNativeClients") {
    doLast { build((rootProject.rootDir)) }
    onlyIf {
        !File(rootProject.rootDir, "src/commonMain/resources/win/ReplayClient.exe").exists()
    }
}

tasks.named("build") {
    dependsOn("buildNativeClients")
}
