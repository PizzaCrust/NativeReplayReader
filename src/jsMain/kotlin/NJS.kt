import kotlin.js.Promise

@JsModule("child_process")
@JsNonModule
internal external object ChildProcess {
    fun exec(cmd: String, block: (error: dynamic, stdout: String, stderr: String) -> Unit)
}

@JsModule("process")
@JsNonModule
internal external object Process {
    val platform: String
    fun cwd(): String
}

@JsName("__dirname")
internal external val currentDirPath: String

@JsModule("path")
@JsNonModule
internal external object Path {
    fun dirname(str: String): String
}

@JsModule("fs")
@JsNonModule
internal external object FileSystem {
    fun existsSync(path: String): Boolean
    fun writeFileSync(path: String, buffer: dynamic)
}

@JsModule("download")
@JsNonModule
external fun download(url: String): Promise<dynamic>