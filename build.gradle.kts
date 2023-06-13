import java.nio.file.Paths

plugins {
    id("java")
    application
    id("org.graalvm.buildtools.native") version "0.9.21"
}

group = "io.sergejisbrecht"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.conscrypt:conscrypt-openjdk-uber:2.5.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

application {
    applicationDefaultJvmArgs = listOf("-Xmx128m", "-Xms128m", "-XX:+UseSerialGC", "-Xlog:gc=debug")
//    applicationDefaultJvmArgs = listOf(
//        "-Xmx512m",
//        "-Xms512m",
//        // "-XX:+UseParallelGC",
//    )
    mainClass.set("io.sergejisbrecht.Main")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        // vendor.set(JvmVendorSpec.matching("GraalVM Community"))
    }
}

graalvmNative {
    binaries {
        named("main") {
            buildArgs.set(
                listOf(
                    "-H:+EnableSecurityServicesFeature",
                    // "-H:+EnableAllSecurityServices",
                    "-H:+TraceSecurityServices",
                    // "-H:AdditionalSecurityProviders=org.conscrypt.OpenSSLProvider",
                    "-J-Xmx25g",
                    "-H:+ReportExceptionStackTraces",
                    // "--initialize-at-build-time=org.conscrypt.EvpMdRef\$SHA256,org.conscrypt.EvpMdRef\$SHA1",
                    // "--initialize-at-build-time=org.conscrypt.Platform,org.conscrypt.OpenSSLProvider,org.conscrypt.NativeCrypto,org.conscrypt.NativeLibraryLoader,org.conscrypt.HostProperties",
                    // "--native-compiler-path=/usr/bin/gcc-10",
                )
            )

            imageName.set("application")
            mainClass.set("io.sergejisbrecht.Main") // The main class to use, defaults to the application.mainClass
            debug.set(false) // Determines if debug info should be generated, defaults to false (alternatively add --debug-native to the CLI)
            verbose.set(true) // Add verbose output, defaults to false
            fallback.set(false) // Sets the fallback mode of native-image, defaults to false

            val toFile =
                Paths.get("/path/to/graalvm")
                    .toFile()
            val specificInstallationToolchainSpec = org.gradle.jvm.toolchain.internal.SpecificInstallationToolchainSpec(
                project.objects,
                toFile
            ).apply {
                languageVersion.set(JavaLanguageVersion.of(17))
                vendor.set(JvmVendorSpec.matching("GraalVM Community"))
            }
            javaLauncher.set(javaToolchains.launcherFor(specificInstallationToolchainSpec))
        }
    }
}