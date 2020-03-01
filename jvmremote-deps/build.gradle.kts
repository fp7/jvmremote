plugins {
    `java-library`
}

tasks.jar {
    manifest {
        attributes["Premain-Class"] = "io.github.fp7.jvmremote.deps.AddDepsToClasspath"
    }
}