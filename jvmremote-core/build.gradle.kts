plugins {
    `java-library`
}

repositories {
    jcenter()
    maven("https://repo.clojars.org")
}

val nrepl by configurations.creating

configurations.compileOnly {
    extendsFrom(nrepl)
}


dependencies {
    nrepl("org.clojure:clojure:1.10.1")
    nrepl("cider:cider-nrepl:0.24.0")
}

tasks.jar {
    manifest {
        attributes["Premain-Class"] = "io.github.fp7.jvmremote.core.NreplStarter"
    }
    from(nrepl.map {
        zipTree(it).matching {
            exclude("cider/nrepl/inlined_deps/orchard/v0v5v6/orchard/misc.clj")
            exclude("cider/nrepl/middleware/classpath.clj")

        }
    })
}