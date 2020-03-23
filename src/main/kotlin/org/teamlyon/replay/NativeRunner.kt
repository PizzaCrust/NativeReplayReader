package org.teamlyon.replay

import com.google.gson.Gson
import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.PumpStreamHandler
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.SystemUtils
import java.io.ByteArrayOutputStream
import java.io.File
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

    private fun extractFile(name: String): File {
        val file = File(name)
        if (file.exists()) {
            file.delete()
        }
        FileUtils.copyInputStreamToFile(this.javaClass.classLoader.getResourceAsStream(name), file)
        file.deleteOnExit()
        return file
    }


    init {
        if (!SystemUtils.IS_OS_LINUX && !SystemUtils.IS_OS_WINDOWS) {
            throw UnsupportedOperationException("Invalid os")
        }
        if (SystemUtils.IS_OS_WINDOWS) {
            client = extractFile("windows-client.exe")
        } else {
            client = extractFile("linux-client")
            DefaultExecutor().execute(CommandLine.parse("chmod 777 ${client.absolutePath}"))
        }
    }

    private val threadPool = Executors.newCachedThreadPool()

    fun processReplay(replayFile: File, body: Replay.() -> Unit): Future<*> {
        if (!replayFile.exists()) {
            throw UnsupportedOperationException("File doesn't exist")
        }
        return threadPool.submit {
            val cmd = "${client.absolutePath} ${replayFile.absolutePath}"
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
