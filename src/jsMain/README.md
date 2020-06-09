## NativeReplayReader for Node
This is the implementation of NRR for Node. This wraps a client based on
 FortniteReplayDecompressor, parses the json and returns an api that is common between multiple
  platforms.

## NPM
The clients are included already in the NPM package, and the package can be used as is.
https://www.npmjs.com/package/nativereplayreader

## Kotlin projects
You can use this in your own kotlin/js node projects. However, ensure that you have the clients
 in your own project. Place the /clients folder with linux, mac, win clients in the build/js
 /packages/yourpackagename in a folder named /clients with all the clients. Otherwise, there will
  be no clients resulting in blank json error.
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