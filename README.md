![JitPack](https://img.shields.io/github/v/release/leo18bernese/Utils)

# Utils

#### A collection of advanced methods and tools designed to simplify the development of Minecraft plugins, created by itz\_leoo.

You are welcome to use this library in your projects, remember to periodically check for updates to benefit from the latest features and improvements.

**I'm going to add a complete documentation for all classes and methods in the future, but for now, you can refer to the code itself to understand how to use it.**

## ⚠️ Important Notice

Due to an issue identified in the `PaginatedMenuBuilder` class, you **__should not__** use any builds prior to `v1.20.0`.

The problem lies in the `getPages()` method, which internally calls `getAllPageItems()`.  
This call can be performance-heavy, especially in scenarios with many iterations, potentially causing noticeable lag spikes when loading a new page.

## Developers API

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>com.github.leo18bernese</groupId>
    <artifactId>Utils</artifactId>
    <version>Tag</version>
</dependency>
```

### Gradle

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

```groovy
dependencies {
    implementation 'com.github.leo18bernese:Utils:Tag'
}
```
