plugins {
    `java-library`
}
repositories {
    jcenter()
}

val nrepl by configurations.creating

configurations {
    implementation {
        extendsFrom(nrepl)
    }
}

dependencies {
    nrepl(project(":jvmremote-core"))
    nrepl("net.bytebuddy:byte-buddy:1.10.8")
}

tasks.jar {
    manifest {
        attributes["Premain-Class"] = "io.github.fp7.jvmremote.spring_boot.SpringBootNreplStarter"
    }
    from(nrepl.map {
        zipTree(it)
    })
}