# SecurityServicesFeature: NPE

## Setup

`gradle.properties`
```
org.gradle.java.installations.paths=/path/to/graalvm
```

`build.gradle.kts`
```
            val toFile =
                Paths.get("/path/to/graalvm")
                    .toFile()
```

Change both paths to a specific GraalVM instance, you want to build your native-image with.

Then change your JDKs `java.security` (./conf/security) by commenting out all security providers except "SUN". 

```
security.provider.1=SUN
# security.provider.2=SunRsaSign
# security.provider.3=SunEC
# security.provider.4=SunJSSE
# security.provider.5=SunJCE
# security.provider.6=SunJGSS
# security.provider.7=SunSASL
# security.provider.8=XMLDSig
# security.provider.9=SunPCSC
# security.provider.10=JdkLDAP
# security.provider.11=JdkSASL
# security.provider.12=SunPKCS11
```

## BUILD

```
./gradlew nativeCompile --no-daemon --rerun-tasks
```

## ERROR: native-image build

```
Fatal error: java.lang.NullPointerException: Cannot invoke "java.util.Set.iterator()" because "services" is null
        at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.SecurityServicesFeature.doRegisterServices(SecurityServicesFeature.java:575)
        at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.SecurityServicesFeature.lambda$registerServices$9(SecurityServicesFeature.java:566)
        at java.base/java.util.concurrent.ConcurrentHashMap.computeIfAbsent(ConcurrentHashMap.java:1708)
        at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.SecurityServicesFeature.registerServices(SecurityServicesFeature.java:565)
        at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.SecurityServicesFeature.registerServices(SecurityServicesFeature.java:544)
        at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.SecurityServicesFeature.lambda$registerServiceReachabilityHandlers$6(SecurityServicesFeature.java:509)
        at org.graalvm.nativeimage.pointsto/com.oracle.graal.pointsto.meta.AnalysisElement$MethodOverrideReachableNotification.lambda$notifyCallback$0(AnalysisElement.java:206)
        at org.graalvm.nativeimage.pointsto/com.oracle.graal.pointsto.meta.AnalysisElement.lambda$execute$1(AnalysisElement.java:219)
    at org.graalvm.nativeimage.pointsto/com.oracle.graal.pointsto.util.CompletionExecutor.executeCommand(CompletionExecutor.java:187)
------------------------------------------------------------------------------------------------------------------------
        at org.graalvm.nativeimage.pointsto/com.oracle.graal.pointsto.util.CompletionExecutor.lambda$executeService$0(CompletionExecutor.java:171)
        at java.base/java.util.concurrent.ForkJoinTask$RunnableExecuteAction.exec(ForkJoinTask.java:1395)
        at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
        at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
        at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
        at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
        at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
                        0.4s (4.0% of total time) in 39 GCs | Peak RSS: 0.73GB | CPU load: 8.25
========================================================================================================================
Finished generating 'application' in 9.2s.
com.oracle.svm.driver.NativeImage$NativeImageError
        at org.graalvm.nativeimage.driver/com.oracle.svm.driver.NativeImage.showError(NativeImage.java:2019)
        at org.graalvm.nativeimage.driver/com.oracle.svm.driver.NativeImage.build(NativeImage.java:1641)
        at org.graalvm.nativeimage.driver/com.oracle.svm.driver.NativeImage.performBuild(NativeImage.java:1600)
        at org.graalvm.nativeimage.driver/com.oracle.svm.driver.NativeImage.main(NativeImage.java:1574)
```
