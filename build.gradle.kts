import io.izzel.taboolib.gradle.*

plugins {
    java
    id("io.izzel.taboolib") version "2.0.37"
    id("org.jetbrains.kotlin.jvm") version "2.3.20"
}

taboolib {
    env {
        install(Velocity, Basic)
    }
    version { taboolib = "6.3.0-afd75a7" }
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.5.0-SNAPSHOT")
    compileOnly(kotlin("stdlib"))
}

kotlin {
    jvmToolchain(21)
}
