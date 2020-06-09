task("buildNativeClients") {
    doLast { build((rootProject.rootDir)) }
    onlyIf {
        !File(rootProject.rootDir, "src/commonMain/resources/win/ReplayClient.exe").exists()
    }
}

task("copyNativeClients") {
    doLast {
        copyContents(File(rootProject.rootDir, "src/commonMain/resources/"), File(rootProject
                .rootDir, "build/js/packages/nativereplayreader/"))
    }
    onlyIf {
        !File(rootProject.rootDir, "build/js/packages/nativereplayreader/clients/win/ReplayClient" +
                ".exe").exists()
    }
    dependsOn("buildNativeClients")
}

tasks.named("build") {
    dependsOn("copyNativeClients")
}
