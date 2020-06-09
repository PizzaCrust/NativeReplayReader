package me.tgsc.replay

import com.google.gson.Gson
import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.PumpStreamHandler
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.SystemUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.RuntimeException
import java.lang.UnsupportedOperationException
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.system.exitProcess

fun main() {
    val runner = NativeRunner()
    runner.processReplay(File("duo 2.replay")) {
        println(this)
    }.get()
    exitProcess(0)
}

class NativeRunner {

    val client: File

    private fun extractFile(platform: SupportedPlatform): File {
        val file = File(platform.fileName)
        if (file.exists()) {
            file.delete()
        }
        FileUtils.copyInputStreamToFile(this.javaClass.classLoader.getResourceAsStream(platform.file), file)
        file.deleteOnExit()
        return file
    }

    internal enum class SupportedPlatform(val current: Boolean, extension: String = "") {
        LINUX(SystemUtils.IS_OS_LINUX),
        WIN(SystemUtils.IS_OS_WINDOWS, "exe"),
        MAC(SystemUtils.IS_OS_MAC_OSX);

        val fileName = StringBuilder("ReplayClient").apply {
            if (extension.isNotBlank()) append(".$extension")
        }.toString()

        val file = "${name.toLowerCase()}/$fileName"

        companion object {
            val currentPlatform = values().firstOrNull { it.current }
        }
    }


    init {
        if (SupportedPlatform.currentPlatform == null) throw UnsupportedOperationException("Invalid os")
        client = extractFile(SupportedPlatform.currentPlatform)
        if (SupportedPlatform.currentPlatform != SupportedPlatform.WIN) {
            DefaultExecutor().execute(CommandLine.parse("chmod 777 ${client.absolutePath}"))
        }
    }

    private val threadPool = Executors.newCachedThreadPool()

    enum class ParseMode {
        EventsOnly, Minimal, Normal, Full, Debug, Ignore
    }

    fun processReplay(replayFile: File, parseMode: ParseMode = ParseMode.Minimal, body: Replay.() -> Unit): Future<*> {
        if (!replayFile.exists()) {
            throw UnsupportedOperationException("File doesn't exist")
        }
        return threadPool.submit {
            val cmd = "${client.absolutePath} \"${replayFile.absolutePath}\" ${parseMode.name}"
            val out = ByteArrayOutputStream()
            val exec = DefaultExecutor()
            exec.streamHandler = PumpStreamHandler(out)
            exec.execute(CommandLine.parse(cmd))
            val str = out.toString()
            val builder = StringBuilder()
            str.lines().forEachIndexed { index, s ->
                if (index == 0) {
                    println(s)
                } else {
                    builder.append("$s\n")
                }
            }
            body(Gson().fromJson(builder.toString(), Replay::class.java))
        }
    }

}
