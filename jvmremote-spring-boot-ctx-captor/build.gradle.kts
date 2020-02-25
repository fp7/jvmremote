plugins {
    `java-library`
}

repositories {
    jcenter()
}

val uberjar by configurations.creating

dependencies {
    uberjar("net.bytebuddy:byte-buddy:1.10.8")
}

configurations.implementation {
    extendsFrom(uberjar)
}

tasks.jar {
    manifest {
        attributes["Premain-Class"] = "io.github.fp7.jvmremote.spring_boot.ctx.SpringBootContextCaptor"
    }
    from(uberjar.map {
        zipTree(it)
    })
}