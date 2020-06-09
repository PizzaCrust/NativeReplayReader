## Native Replay Reader
Native Replay Reader wraps and provides a high level interface from clients that run natively on Mac
, Linux, and Windows
 that parse replays and dump the result as JSON through FortniteReplayDecompressor. These clients
  are auto
  generated if they are
  missing.
## Usage
```
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("com.github.PizzaCrust.NativeReplayReader:nativereplayreader-jvm:4297639511")
}
```
## Platform support
Currently, **JVM** has the best support with all-in-one; however JS (node) is semi-supported
 requiring
 to download the client on first launch. In the future, Kotlin/native will be supported.
 
## TODO
- Shorten dependencies list of JVM implementation
- Remove requirement of download npm module for JS
- Implement kotlin/native support