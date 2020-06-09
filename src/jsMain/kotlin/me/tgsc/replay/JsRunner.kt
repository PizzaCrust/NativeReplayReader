package me.tgsc.replay

import FileSystem
import currentDirPath
import download
import kotlin.js.Promise

//val clientsPath = "${Path.dirname(currentDirPath)}/clients"

/*
fun main() {
    JsRunner().processReplay("${Process.cwd()}/solo win.replay") {
        println(this)
    }
}
 */

class JsRunner: ReplayParser<String, JsRunner.PromiseTicket> {

    class PromiseTicket(private val promise: Promise<Replay>): ReplayParser.Ticket {
        private var internalCompleted = false
        override val completed: Boolean
            get() = internalCompleted
        fun finished(block: Replay?.() -> Unit) {
            promise.then({
                internalCompleted = true
                block(it)
            }, {
                internalCompleted = true
                block(null)
            })
        }
    }

    internal enum class SupportedPlatform(val current: Boolean, extension: String = "") {
        LINUX(Process.platform == "linux"),
        WIN(Process.platform == "win32", "exe"),
        MAC(Process.platform == "darwin");

        val fileName = StringBuilder("ReplayClient").apply {
            if (extension.isNotBlank()) append(".$extension")
        }.toString()

        val file = "https://github.com/PizzaCrust/NativeReplayReader/raw/master/src/commonMain/resources/${name.toLowerCase()}/$fileName"

        companion object {
            val currentPlatform = values().firstOrNull { it.current }
        }
    }

    init {
        if (SupportedPlatform.currentPlatform == null) throw UnsupportedOperationException("OS not supported")
    }

    private fun execReplayClient(replayClientPath: String,
                                 inputResource: String,
                                 parseMode: ReplayParser.ParseMode,
                                 block: Replay.() -> Unit,
                                 resolve: (Replay) -> Unit,
                                 reject: (Throwable) -> Unit) {
        println("Parsing replay")
        ChildProcess.exec("$replayClientPath \"$inputResource\" " +
                parseMode.name) { _, stdout, _ ->
            try {
                val str = StringBuilder().apply {
                    stdout.split("\n").toMutableList().apply {
                        println(this[0])
                        removeAt(0)
                    }.forEach {
                        append(it).append("\n")
                    }
                }.toString()
                Replay.fromJson(str).apply {
                    block(this)
                    resolve(this)
                }
            } catch (e: Exception) {
                reject(e)
            }
        }
    }

    // in js the files should be chmodded already i hvaent done this yet
    override fun processReplay(inputResource: String, parseMode: ReplayParser.ParseMode, block: Replay.() -> Unit): PromiseTicket {
        if (!FileSystem.existsSync(inputResource)) throw UnsupportedOperationException("File doesn't exist")
        val replayClientPath = "${Process.cwd()}/${SupportedPlatform.currentPlatform!!.fileName}"
        return PromiseTicket(Promise { resolve, reject ->
            if (!FileSystem.existsSync(replayClientPath)) {
                //val stream = FileSystem.createWriteStream(replayClientPath)
                println("Downloading respective os client")
                download(SupportedPlatform.currentPlatform.file).then<dynamic> {
                    println("Download finished")
                    FileSystem.writeFileSync(replayClientPath, it)
                    execReplayClient(replayClientPath, inputResource, parseMode, block, resolve, reject)
                }
            } else {
                println("Invoking existing os client")
                execReplayClient(replayClientPath, inputResource, parseMode, block, resolve, reject)
            }
        })
    }

}