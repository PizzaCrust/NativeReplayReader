## NativeReplayReader for Node
This is the implementation of NRR for Node. This wraps a client based on
 FortniteReplayDecompressor, parses the json and returns an api that is common between multiple
  platforms. It downloads the required client per supported operating system for first launch
   from this repository.

## NPM
The clients are included already in the NPM package, and the package can be used as is. However, in versions higher than 2.0.0, they will no longer be included.
https://www.npmjs.com/package/nativereplayreader

## Kotlin projects
You can use this in your own kotlin/js node projects. However, the clients are not included by
 default the required client will be downloaded on first launch.
```
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("com.github.PizzaCrust.NativeReplayReader:nativereplayreader-js:4297639511")
    implementation(npm("download", "8.0.0"))
}
```