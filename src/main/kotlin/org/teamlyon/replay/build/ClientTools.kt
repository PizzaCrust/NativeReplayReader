package org.teamlyon.replay.build

import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.io.FileUtils
import org.eclipse.jgit.api.Git
import org.teamlyon.replay.NativeRunner
import java.io.File
import java.net.URL

private fun File.exec(cmd: String) {
    val exec = DefaultExecutor()
    exec.workingDirectory = this
    exec.execute(CommandLine.parse(cmd))
}

private fun File.extractFrom(str: String) {
    val target = File(this, str)
    FileUtils.copyInputStreamToFile(NativeRunner::class.java.classLoader.getResourceAsStream(str), target)
}

private fun File.build(runtime: String): File {
    exec("dotnet publish -r $runtime -c Release /p:PublishSingleFile=true")
    return File(this, "bin/Release/netcoreapp3.1/$runtime/publish/")
}

private fun File.move(dest: File) {
    this.listFiles()?.forEach {
        FileUtils.moveFileToDirectory(it, dest, true)
    }
}

/**
 * NOT INTENDED FOR PRODUCTION
 */
fun main() {
    val tmp = File("tmp")
    if (tmp.exists()) {
        tmp.deleteRecursively()
    }
    tmp.mkdir()
    println("Cloning latest FortniteReplayDecompressor repository")
    Git
            .cloneRepository()
            .setURI("https://github.com/Shiqan/FortniteReplayDecompressor.git")
            .setDirectory(File(tmp, "FortniteReplayDecompressor"))
            .call()
    println("Building FortniteReplayDecompressor")
    File(tmp, "FortniteReplayDecompressor/src/FortniteReplayReader").extractFrom("FRR.csproj")
    File(tmp, "FortniteReplayDecompressor/src/FortniteReplayReader").exec("dotnet build FRR.csproj")
    println("Extracting client")
    val replayClient = File(tmp, "ReplayClient")
    replayClient.mkdir()
    replayClient.extractFrom("Program.cs")
    replayClient.extractFrom("ReplayClient.csproj")
    val targetBuild = File("nativeClientBin")
    if (targetBuild.exists()) {
        targetBuild.deleteRecursively()
    }
    targetBuild.mkdir()
    println("Building windows client")
    replayClient.build("win-x64").move(File(targetBuild, "win"))
    println("Building linux client")
    replayClient.build("linux-x64").move(File(targetBuild, "linux"))
    println("Building mac os client")
    replayClient.build("osx-x64").move(File(targetBuild, "mac"))
    tmp.deleteRecursively()
}