subprojects.forEach { _ ->
    group = "io.github.fp7.jvmremote"

    plugins.withType(JavaLibraryPlugin::class) {
        val jpe = extensions.getByType(JavaPluginExtension::class)
        jpe.sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
        jpe.targetCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
    }
}


