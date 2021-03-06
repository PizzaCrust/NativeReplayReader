import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.io.FileUtils
import org.eclipse.jgit.api.Git
import java.io.File

private fun File.exec(cmd: String) {
    val exec = DefaultExecutor()
    exec.workingDirectory = this
    exec.execute(CommandLine.parse(cmd))
}

private fun File.extractFrom(str: String) {
    val target = File(this, str)
    FileUtils.copyInputStreamToFile(Thread.currentThread().contextClassLoader.getResourceAsStream(str), target)
}

private fun File.build(runtime: String): File {
    exec("dotnet publish -r $runtime -c Release /p:PublishSingleFile=true /p:PublishTrimmed=true")
    return File(this, "bin/Release/netcoreapp3.1/$runtime/publish/")
}

private fun File.move(dest: File) {
    this.listFiles()?.forEach {
        FileUtils.moveFileToDirectory(it, dest, true)
    }
}

private fun File.deleteAll() {
    this.listFiles()?.forEach {
        if (it.isDirectory) {
            it.deleteAll()
        } else {
            it.delete()
        }
    }
    delete()
}

/**
 * NOT INTENDED FOR PRODUCTION
 */
fun build(root: File) {
    val tmp = File(root, "build/nativeTmp").apply {
        if (exists()) deleteAll()
        mkdir()
    }
    println("Cloning latest FortniteReplayDecompressor repository")
    Git
            .cloneRepository()
            .setURI("https://github.com/Shiqan/FortniteReplayDecompressor.git")
            .setDirectory(File(tmp, "FortniteReplayDecompressor"))
            .call().apply {
                repository.close()
                close()
            }
    println("Building FortniteReplayDecompressor")
    File(tmp, "FortniteReplayDecompressor/src/FortniteReplayReader").extractFrom("FRR.csproj")
    File(tmp, "FortniteReplayDecompressor/src/FortniteReplayReader").exec("dotnet build FRR.csproj")
    println("Transpiling models to kotlin")
    val frrModels = File(tmp, "FortniteReplayDecompressor/src/FortniteReplayReader/Models")
    val unrealCoreModels = File(tmp, "FortniteReplayDecompressor/src/Unreal.Core/Models")
    File(root, "ReplayModel.kt").writeText(generateKotlinDirSrc(listOf(frrModels, unrealCoreModels)).replace
    ("ActorGuid",
            "NetworkGUID"))
    println("Extracting client")
    val replayClient = File(tmp, "ReplayClient")
    replayClient.mkdir()
    replayClient.extractFrom("Program.cs")
    replayClient.extractFrom("ReplayClient.csproj")
    val targetBuild = File(root, "src/commonMain/resources")
    println("Building windows client")
    replayClient.build("win-x64").move(File(targetBuild, "win"))
    println("Building linux client")
    replayClient.build("linux-x64").move(File(targetBuild, "linux"))
    println("Building mac os client")
    replayClient.build("osx-x64").move(File(targetBuild, "mac"))
    tmp.deleteAll()
}