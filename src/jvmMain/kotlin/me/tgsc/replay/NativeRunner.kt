package me.tgsc.replay

import me.tgsc.replay.Replay.Companion.fromJson
import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.PumpStreamHandler
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.UnsupportedOperationException
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.system.exitProcess

fun main() {
    val runner = NativeRunner()
    runner.processReplay(File("test.replay")) {
        println(this)
    }.future.get()
    exitProcess(0)
}

internal val os: String = System.getProperty("os.name").toLowerCase()

//todo remove all external dependencies
class NativeRunner: ReplayParser<File, NativeRunner.FutureTicket> {

    val client: File

    private fun extractFile(platform: SupportedPlatform): File {
        val file = File(platform.fileName)
        if (file.exists()) {
            file.delete()
        }
        file.writeBytes(this.javaClass.classLoader.getResourceAsStream(platform.file)!!.readBytes())
        file.deleteOnExit()
        return file
    }

    internal enum class SupportedPlatform(val current: Boolean, extension: String = "") {
        LINUX(os.startsWith("linux")),
        WIN(os.startsWith("windows"), "exe"),
        MAC(os.startsWith("Mac OS X"));
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

    class FutureTicket(val future: Future<*>): ReplayParser.Ticket {
        override val completed
            get() = future.isDone
    }

    override fun processReplay(inputResource: File, parseMode: ReplayParser.ParseMode, block: Replay.() -> Unit): FutureTicket {
        if (!inputResource.exists()) {
            throw UnsupportedOperationException("File doesn't exist")
        }
        return FutureTicket(threadPool.submit {
            val cmd = "${client.absolutePath} \"${inputResource.absolutePath}\" ${parseMode.name}"
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
            block(fromJson(builder.toString()))
        })
    }

}
