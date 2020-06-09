package me.tgsc.replay

import currentDirPath
import kotlin.js.Promise

val clientsPath = "${Path.dirname(currentDirPath)}/clients"

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

        val file = "$clientsPath/${name.toLowerCase()}/$fileName"

        companion object {
            val currentPlatform = values().firstOrNull { it.current }
        }
    }

    init {
        if (SupportedPlatform.currentPlatform == null) throw UnsupportedOperationException("OS not supported")
    }

    // in js the files should be chmodded already i hvaent done this yet
    override fun processReplay(inputResource: String, parseMode: ReplayParser.ParseMode, block: Replay.() -> Unit): PromiseTicket {
        if (!FileSystem.existsSync(inputResource)) throw UnsupportedOperationException("File doesn't exist")
        return PromiseTicket(Promise { resolve, reject ->
            ChildProcess.exec("${SupportedPlatform.currentPlatform!!.file} \"$inputResource\" " +
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
        })
    }

}