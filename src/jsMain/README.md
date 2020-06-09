## NativeReplayReader for Node
This is the implementation of NRR for Node. This wraps a client based on
 FortniteReplayDecompressor, parses the json and returns an api that is common between multiple
  platforms.

## NPM
https://www.npmjs.com/package/nativereplayreader

## Kotlin projects
You can use this in your own kotlin/js node projects.
```
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("com.github.PizzaCrust.NativeReplayReader:nativereplayreader-js:68b127343e")
}
```