@JsModule("child_process")
@JsNonModule
external object ChildProcess {
    fun exec(cmd: String, block: (error: dynamic, stdout: String, stderr: String) -> Unit)
}

@JsModule("process")
@JsNonModule
external object Process {
    val platform: String
}

@JsName("__dirname")
external val currentDirPath: String

@JsModule("path")
@JsNonModule
external object Path {
    fun dirname(str: String): String
}

@JsModule("fs")
@JsNonModule
external object FileSystem {
    fun existsSync(path: String): Boolean
}